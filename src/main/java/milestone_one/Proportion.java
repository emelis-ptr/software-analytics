package milestone_one;

import entity.Release;
import entity.Ticket;
import entity.TicketJira;

import java.util.ArrayList;
import java.util.List;

public class Proportion {
    private Proportion() {
    }

    private static final ArrayList<Integer> proportions = new ArrayList<>();


    /**
     * Inserisce la FV e per ogni ticket calcolare la versione dell'IV tramite il
     * metodo proportion incrementale
     *
     * @param releases:    lista delle release
     * @param ticketJiras: lista dei ticket presenti su Jira
     * @param tickets:     lista dei ticket che contengono nel commit id del ticket
     */
    public static void proportionMethod(List<Release> releases, List<TicketJira> ticketJiras, List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            setFV(ticket, releases);
        }
        proportion(ticketJiras, releases);
    }

    /**
     * Calcola la IV di un tickets. Se IV presente calculata p altrimenti
     * applica il metodo proportion
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     * @param releases:    lista delle release
     */
    private static void proportion(List<TicketJira> ticketJiras, List<Release> releases) {
        for (TicketJira version : ticketJiras) {
            if (version.getInjectedVersion() != null) {
                calculateP(releases, version);
            } else {
                calculateIV(releases, version);
            }
        }
    }

    /***
     * Per ogni Ticket sostituisce FV se l'ultima data del commit in git è uguale
     * o avviene dopo la data di release
     *
     * @param ticket: ticket
     * @param releases: lista delle release
     **/
    private static void setFV(Ticket ticket, List<Release> releases) {
        for (Release release : releases) {
            if (ticket.getLastDateCommit().equals(release.getDateCreation()) || release.getDateCreation().isAfter(ticket.getLastDateCommit())) {
                ticket.getTicket().setFixedVersion(release);
                break;
            } else {
                ticket.getTicket().setFixedVersion(releases.get(releases.size() - 1));
            }
        }
    }


    /**
     * Calcola proportion se il tickets ha IV
     *
     * @param releases:   lista delle release
     * @param ticketJira: ticket presente su Jira
     */
    private static void calculateP(List<Release> releases, TicketJira ticketJira) {
        int p;
        int ivCount = 0;
        int fvCount = 0;
        int ovCount = 0;
        int count = 1;

        // per ogni release conta gli IV, OV e FV
        for (Release release : releases) {
            if (release.getReleaseID().equals(ticketJira.getInjectedVersion().getReleaseID())) {
                ivCount = count;
            }
            if (release.getReleaseID().equals(ticketJira.getOpeningVersion().getReleaseID())) {
                ovCount = count;
            }
            if (release.getReleaseID().equals(ticketJira.getFixedVersion().getReleaseID())) {
                fvCount = count;
            }
            count++;
        }
        // se OV è diverso da FV allora calcola p
        if (ovCount != fvCount) {
            p = (fvCount - ivCount) / (fvCount - ovCount);
            proportions.add(p);
        }
    }

    /**
     * Metodo che utilizza proportion incrementale per stimare IV
     *
     * @param releases:
     * @param ticketJira:
     */
    private static void calculateIV(List<Release> releases, TicketJira ticketJira) {
        int ivNumber;
        int fvNumber = 0;
        int ovNumber = 0;
        int pMeanValue = 0;

        int count = 1;
        // per ogni release conta il numero di OV, FV
        for (Release release : releases) {
            if (release.getReleaseID().equals(ticketJira.getOpeningVersion().getReleaseID())) {
                ovNumber = count;
            }
            if (release.getReleaseID().equals(ticketJira.getFixedVersion().getReleaseID())) {
                fvNumber = count;
            }
            count++;
        }
        int sumP = 0;

        // si sommano tutti i valori della lista proportions
        for (Integer proportion : proportions) {
            sumP += proportion;
        }

        // si calcola il valore medio di p per stimare la IV
        if (!proportions.isEmpty()) {
            pMeanValue = sumP / proportions.size();
        }
        // se FV è diverso dalla OV si calcola IV
        if (!ticketJira.getFixedVersion().getDateCreation().isEqual(ticketJira.getOpeningVersion().getDateCreation())) {
            ivNumber = fvNumber - ((fvNumber - ovNumber) * pMeanValue);
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
}
