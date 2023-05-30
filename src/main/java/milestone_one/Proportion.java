package milestone_one;

import entity.Release;
import entity.Ticket;
import entity.TicketJira;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Proportion {
    private Proportion() {
    }

    private static final ArrayList<Integer> proportions = new ArrayList<>();


    /**
     * Calcola la IV di un tickets. Se IV presente calculata p altrimenti
     * applica il metodo proportion
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     * @param releases:    lista delle release
     */
    public static void proportion(List<TicketJira> ticketJiras, List<Release> releases) {
        ticketJiras.stream()
                .filter(Ticket::isContained)
                //ordiniamo in base all'ultimo fix del commit che contiene l'id del ticket
                .sorted(Comparator.comparing(TicketJira::getLastDateCommit))
                .forEach(ticketJira -> {
                    ticketJira.setFV(releases);
                    if (ticketJira.getInjectedVersion() != null) {
                        calculateP(ticketJira);
                    } else {
                        calculateIV(releases, ticketJira);
                    }
                });
    }

    /**
     * Calcola proportion se il tickets ha IV
     *
     * @param ticketJira: ticket presente su Jira
     */
    private static void calculateP(TicketJira ticketJira) {
        int p;
        int ivCount = ticketJira.getInjectedVersion().getNumVersion();
        int fvCount = ticketJira.getFixedVersion().getNumVersion();
        int ovCount = ticketJira.getOpeningVersion().getNumVersion();

        // se OV è diverso da FV allora calcola p
        if (ovCount != fvCount) {
            p = (fvCount - ivCount) / (fvCount - ovCount);
            proportions.add(p);
        } else {
            // se OV = FV allora P= (FV-IV)/(OV-IV) = 1
            proportions.add(1);
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
        int fvNumber = ticketJira.getFixedVersion().getNumVersion();
        int ovNumber = ticketJira.getOpeningVersion().getNumVersion();
        int pMeanValue = 0;

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
        if (fvNumber != ovNumber) {
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
