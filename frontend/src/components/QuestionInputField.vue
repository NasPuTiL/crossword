<template>
    <v-layout justify-center align-center column pa-5>
        <v-col cols="12" md="1" sm="1">
            <div class="hidden-sm-and-down py-12" style="min-height: 20vh;" />
        </v-col>
        <v-spacer class="hidden-sm-and-down"></v-spacer>
        <v-col cols="12" sm="6" md="9">
            <v-text-field
                label="Wpisz pytanie"
                outlined
                color="grey-lighten-6"
                clearable
                hide-details
                v-model="tempMessage"
                @input="submit"
                >{{ tempMessage }}</v-text-field
            >
        </v-col>
        <!-- this part is left for example purposes 
            <v-btn fab class="mt-5 brown darken-4">
                <v-icon large color="white">mdi-chevron-down</v-icon>
            </v-btn>
            -->
    </v-layout>
</template>

<script>
export default {
    name: 'QuestionInputField',
    data() {
        return {
            tempMessage: ''
        };
    },
    methods: {
        submit: function() {
            this.$emit('inputData', this.tempMessage);
            //this.tempMessage = "";
        }
    }
};
</script>

<style scoped></style>
