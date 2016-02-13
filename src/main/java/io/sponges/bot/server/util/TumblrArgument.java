package io.sponges.bot.server.util;

public class TumblrArgument {

    private final String[] intros = {
            "burn in hell",
            "check your privilege",
            "fuck you",
            "fuck off",
            "please die",
            "rot in hell",
            "screw you",
            "shut the fuck up",
            "shut up",
            "kill yourself",
            "drop dead"
    };

    private final String[] description = {
            "deluded",
            "fucking",
            "god damn",
            "judgemental",
            "worthless"
    };

    private final String[][] marginalized = {
            {
                    "activist",
                    "agender",
                    "appearance",
                    "asian",
                    "attractive",
                    "bi",
                    "bigender",
                    "black",
                    "celestial",
                    "chubby",
                    "closet",
                    "color",
                    "curvy",
                    "dandy",
                    "deathfat",
                    "demi",
                    "differently abled",
                    "disabled",
                    "diversity",
                    "dysphoria",
                    "ethnic",
                    "ethnicity",
                    "fat love",
                    "fat",
                    "fatist",
                    "fatty",
                    "female",
                    "feminist",
                    "genderfuck",
                    "genderless",
                    "hair",
                    "height",
                    "indigenous",
                    "intersectionality",
                    "invisible",
                    "kin",
                    "lesbianism",
                    "little person",
                    "marginalized",
                    "minority",
                    "multigender",
                    "non-gender",
                    "non-white",
                    "obesity",
                    "otherkin",
                    "pansexual",
                    "polygender",
                    "privilege",
                    "prosthetic",
                    "queer",
                    "radfem",
                    "skinny",
                    "smallfat",
                    "stretchmark",
                    "thin",
                    "third-gender",
                    "trans",
                    "transfat",
                    "transgender",
                    "transman",
                    "transwoman",
                    "trigger",
                    "two-spirit",
                    "womyn",
                    "poc",
                    "woc",
            },
            {
                    "chauvinistic",
                    "misogynistic",
                    "nphobic",
                    "oppressive",
                    "phobic",
                    "shaming",
                    "denying",
                    "discriminating",
                    "hypersexualizing",
                    "racist",
                    "intolerant",
                    "sexualizing",
            }
    };

    private final String[][] privileged = {
            {
                    "able-bodied",
                    "appearance",
                    "attractive",
                    "binary",
                    "bi",
                    "cis",
                    "cisgender",
                    "cishet",
                    "hetero",
                    "male",
                    "smallfat",
                    "thin",
                    "white"
            },
            {
                    "ableist",
                    "classist",
                    "normative",
                    "overprivileged",
                    "patriarch",
                    "sexist",
                    "privileged"
            }
    };

    private final String[] finisher = {
            "asshole",
            "bigot",
            "oppressor",
            "piece of shit",
            "rapist",
            "scum",
            "shitlord",
            "subhuman",
            "misogynist",
            "nazi"
    };

    private final String[][] terms = {
            {
                    "a",
                    "bi",
                    "dandy",
                    "demi",
                    "gender",
                    "multi",
                    "pan",
                    "poly"
            },
            {
                    "amorous",
                    "femme",
                    "fluid",
                    "queer",
                    "romantic",
                    "sexual"
            }
    };

    public TumblrArgument() {

    }

    private String getRandomItem(String[] array) {
        return array[((int) Math.floor(Math.random() * array.length))];
    }

    private String generateTerm() {
        return getRandomItem(terms[0]) + getRandomItem(terms[1]);
    }

    public String generateArgument() {
        StringBuilder builder = new StringBuilder();
        builder.append(getRandomItem(intros)).append(", you ");
        builder.append(getRandomItem(description)).append(" ");
        builder.append(getRandomItem(new String[]{
                generateTerm(),
                getRandomItem(marginalized[0])
        })).append("-");
        builder.append(getRandomItem(marginalized[1])).append(", ");
        builder.append(getRandomItem(privileged[0])).append("-");
        builder.append(getRandomItem(privileged[1])).append(" ");
        builder.append(getRandomItem(finisher)).append(".");

        return builder.toString();
    }

}
