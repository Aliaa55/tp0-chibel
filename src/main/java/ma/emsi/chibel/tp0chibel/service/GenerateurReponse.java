package ma.emsi.chibel.tp0chibel.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;

/**
 * Classe de service qui construit la réponse à une question.
 * Traitement choisi : analyse de la question (mots, voyelles, consonnes, mot le plus long)
 * suivie d'un chiffrement de César dont le décalage dépend du nombre de mots.
 */
@Dependent // Portée CDI : l'instance est supprimée quand le backing bean qui l'a injectée est supprimé.
public class GenerateurReponse implements Serializable {

    private static final String VOYELLES = "aeiouyàâäéèêëîïôöùûüÿœæ";

    /**
     * Analyse la question puis la chiffre avec un César.
     *
     * @param question    La question de l'utilisateur.
     * @param roleSysteme Le rôle système (non null seulement pour la première question), ou null.
     * @return Le rapport d'analyse suivi de la question chiffrée.
     */
    public String genererReponse(String question, String roleSysteme) {
        String[] mots = question.trim().split("\\s+");
        int nbMots = mots.length;

        int nbVoyelles = 0;
        int nbConsonnes = 0;
        for (char c : question.toCharArray()) {
            if (Character.isLetter(c)) {
                if (VOYELLES.indexOf(Character.toLowerCase(c)) >= 0) {
                    nbVoyelles++;
                } else {
                    nbConsonnes++;
                }
            }
        }

        String motLePlusLong = "";
        for (String mot : mots) {
            String propre = mot.replaceAll("[^\\p{L}]", "");
            if (propre.length() > motLePlusLong.length()) {
                motLePlusLong = propre;
            }
        }

        int decalage = (nbMots % 25) + 1;

        StringBuilder resultat = new StringBuilder("-- Analyse --\n");
        if (roleSysteme != null) {
            String premiereLigne = roleSysteme.strip().lines().findFirst().orElse("");
            resultat.append("Rôle : ").append(premiereLigne).append("\n");
        }
        resultat.append("Mots : ").append(nbMots).append("\n");
        resultat.append("Voyelles : ").append(nbVoyelles).append("\n");
        resultat.append("Consonnes : ").append(nbConsonnes).append("\n");
        resultat.append("Mot le plus long : ").append(motLePlusLong).append("\n");
        resultat.append("Code César (décalage ").append(decalage).append(") : ")
                .append(cesar(question, decalage)).append("\n");
        return resultat.toString();
    }

    /**
     * Chiffrement de César : décale les lettres a-z et A-Z, laisse le reste inchangé.
     */
    private String cesar(String texte, int decalage) {
        StringBuilder sb = new StringBuilder();
        for (char c : texte.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + (c - 'a' + decalage) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + (c - 'A' + decalage) % 26));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}