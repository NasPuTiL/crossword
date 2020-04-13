<template>
    <v-layout justify-center align-center column pa-5>
        <v-spacer class="hidden-sm-and-down"></v-spacer>
        <v-col cols="12" sm="6" md="9">
            <v-card v-for="value in message" :key="value.id">
                <v-list-item style="height: auto;">
                    <v-list-item-title> WARTOŚĆ: "{{ value.key}}", ilość wystąpień: {{value.occurrences}}.</v-list-item-title>
                </v-list-item>
            </v-card>
            
        </v-col>
    </v-layout>
</template>

<script>
export default {
    name: 'PossibleEntries',
    props: ["msg"],
    data: function() {
        return {
            message: [],
            pos: 1,
        };
    },
    watch: {
        msg: function() {
            this.message = this.msg;
        }
    },
    methods: {

    }
};
</script>

<style scoped></style>
