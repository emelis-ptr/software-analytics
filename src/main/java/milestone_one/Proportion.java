package milestone_one;

import entity.Release;
import entity.Ticket;
import entity.TicketJira;
import enums.Projects;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.GitHandler;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static milestone_one.MilestoneOne.*;
import static util.Utils.path;
import static util.Utils.project;

public class Proportion {
    private Proportion() {
    }

    protected static final ArrayList<Double> proportions = new ArrayList<>();

    /**
     * Calcola la IV di un tickets. Se IV presente calculata p altrimenti
     * applica il metodo proportion
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     * @param releases:    lista delle release
     */
    public static void proportion(List<TicketJira> ticketJiras, List<Release> releases, List<Double> proportion, double pColdStart ) {

        ticketJiras.stream()
                .filter(Ticket::isContainedInACommit)
                .filter(ticketJira -> ticketJira.getOpeningVersion() != null)
                //ordiniamo in base all'ultimo fix del commit che contiene l'id del ticket
                .sorted(Comparator.comparing(TicketJira::getLastDateCommit))
                .forEach(ticketJira -> {
                    if (ticketJira.getFixedVersion() == null) {
                        ticketJira.setFV(releases);
                    }
                    if (ticketJira.getInjectedVersion() != null) {
                        calculateP(ticketJira, proportion);
                    } else {
                        calculateIV(releases, ticketJira, proportion, pColdStart);
                    }
                });
    }

    /**
     * Calcola proportion se il tickets ha IV
     *
     * @param ticketJira: ticket presente su Jira
     */
    private static void calculateP(TicketJira ticketJira, List<Double> proportion) {
        double p;
        int ivCount = ticketJira.getInjectedVersion().getNumVersion();
        int fvCount = ticketJira.getFixedVersion().getNumVersion();
        int ovCount = ticketJira.getOpeningVersion().getNumVersion();

        // se OV è diverso da FV allora calcola p
        if (ovCount != fvCount) {
            p =  ((double) fvCount - ivCount) / (fvCount - ovCount);
            proportion.add(p);
        } else {
            // se OV = FV allora P= (FV-IV)/(OV-IV) = 1
            proportion.add(1.0);
        }
    }

    /**
     * Metodo che utilizza proportion incrementale per stimare IV
     *
     * @param releases:
     * @param ticketJira:
     */
    private static void calculateIV(List<Release> releases, TicketJira ticketJira, List<Double> proportion, double pColdStart) {
        int ivNumber;
        int fvNumber = ticketJira.getFixedVersion().getNumVersion();
        int ovNumber = ticketJira.getOpeningVersion().getNumVersion();
        double pMeanValue;

        double sumP = 0;
        // si sommano tutti i valori della lista proportions
        for (double p : proportion) {
            sumP += p;
        }

        // si calcola il valore medio di p per stimare la IV
        if (!proportion.isEmpty()) {
            pMeanValue = sumP / proportion.size();
        } else {
            pMeanValue = pColdStart;
        }

        proportion.add(pMeanValue);

        // se FV è diverso dalla OV si calcola IV
        if (fvNumber != ovNumber) {
            ivNumber = (int) (fvNumber - ((fvNumber - ovNumber) * pMeanValue));
            // non possiamo assegnare un numero di versione negativo, quindi nel caso in cui
            // IV risulta essere minore di 0 si assegna a ivNumber il valore 1
            if (ivNumber <= 0) {
                ivNumber = 1;
            }
        } else { // Se OV==FV, poniamo IV uguale alla OV
            ivNumber = ovNumber;
        }
        ticketJira.setInjectedVersion(releases.get(ivNumber - 1));
    }

    /**
     * Metodo che utilizza proportion ColdStart per stimare IV
     * @return:
     * @throws IOException:
     * @throws ParseException:
     * @throws GitAPIException:
     */
    static double calculateColdStart() throws IOException, ParseException, GitAPIException {
        List<Double> averages = new ArrayList<>();
        double totalAvg;
        double totalSum = 0;

        /* iterazione su tutti i progetti */
        for (Projects.ProjectColdStart proj : Projects.ProjectColdStart.values()) {
            String name = project(proj.toString());
            if (name.equals(PROJ_NAME_M1)) continue;
            GitHandler.cloneRemote(GitHandler.returnPath(path("")).toFile(), name);

            List<Double> proportion = new ArrayList<>();
            double sum = 0;
            double avg;

            List<Release> releases = RetrieveRelease.retrieveRelease(name);
            List<TicketJira> ticketJiras = RetrieveTicketsJira.retrieveTicketJira(releases, name);
            RetrieveTicketGit.getCommits(ticketJiras, releases, name);
            proportion(ticketJiras, releases, proportion, 0);

            for (double p : proportion) {
                sum += p;
            }

            avg = sum / proportion.size();
            averages.add(avg);
        }

        /* iterazione di tutte le medie di P di tutti i progetti esterni */
        for (Double averageP: averages) {
            totalSum += averageP;
        }
        totalAvg = totalSum / averages.size();
        return totalAvg;
    }
}
