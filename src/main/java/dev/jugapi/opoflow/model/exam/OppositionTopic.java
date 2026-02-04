package dev.jugapi.opoflow.model.exam;

public enum OppositionTopic {
    TAI_ALL(Opposition.TAI_AGE_PART1, "Test de preguntas de todos los bloques de TAI_AGE", true),
    TAI_BLOCK1(Opposition.TAI_AGE_PART1, "Test de preguntas del bloque 1 de TAI_AGE", false),
    TAI_BLOCK2(Opposition.TAI_AGE_PART1, "Test de preguntas del bloque 2 de TAI_AGE", false),
    TAI_BLOCK3(Opposition.TAI_AGE_PART1, "Test de preguntas del bloque 3 de TAI_AGE", false),
    TAI_BLOCK4(Opposition.TAI_AGE_PART1, "Test de preguntas del bloque 4 de TAI_AGE", false);

    private final Opposition opposition;
    private final String description;
    private final boolean isGeneral;  // contain another topic?

    OppositionTopic(Opposition opposition, String description, boolean isGeneral) {
        this.opposition = opposition;
        this.description = description;
        this.isGeneral = isGeneral;
    }

    public Opposition getOpposition() {
        return opposition;
    }

    public String getDescription() {
        return description;
    }

    public boolean isGeneral() {
        return isGeneral;
    }

    /* this method allow to filter in service the questions by block or select all */
    public boolean includes(OppositionTopic other) {
        if(other == null){
            return false;
        }
        if (this.isGeneral) {
            return this.opposition == other.getOpposition();
        }
        return this == other;
    }
}
