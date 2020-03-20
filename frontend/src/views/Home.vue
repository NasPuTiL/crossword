<template>
    <v-container fluid class="input-background" style="max-height: 100vh;">
        <question-input-field @inputData="updateMessage"></question-input-field>
        <possible-entries :msg="childData"></possible-entries>
    </v-container>
</template>

<script>
import QuestionInputField from '@/components/QuestionInputField';
import PossibleEntries from '@/components/PossibleEntries';

export default {
    name: 'home',
    components: {
        QuestionInputField,
        PossibleEntries
    },
    data() {
        return {
            childData: ''
        };
    },
    methods: {
        updateMessage(variable) {
            this.childData = variable;
        }
    }
};
</script>
