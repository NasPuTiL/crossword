<template>
    <v-container fluid class="input-background" style="max-height: 100vh;">
        <div>
            <question-input-field @inputData="updateMessage"></question-input-field>
            <possible-entries :msg="childData"></possible-entries>
        </div>
    </v-container>
</template>

<script>
import QuestionInputField from '@/components/QuestionInputField';
import PossibleEntries from '@/components/PossibleEntries';

export default {
    name: 'Home',
    components: {
        QuestionInputField,
        PossibleEntries
    },
    data() {
        return {
            childData: JSON,
        };
    },
    methods: {
        updateMessage(variable) {
            this.childData = variable;
        },
    }
};
</script>
