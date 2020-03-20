<template>
    <v-layout justify-center align-center column pa-5>
        <v-spacer class="hidden-sm-and-down"></v-spacer>
        <v-col cols="12" sm="6" md="9">
            <v-card>
                <v-list-item style="height: auto;">
                    <v-list-item-title>{{ message }}</v-list-item-title>
                </v-list-item>
            </v-card>
        </v-col>
    </v-layout>
</template>

<script>
export default {
    name: 'PossibleEntries',
    props: {
        msg: {
            type: String
        }
    },
    data: function() {
        return {
            message: ''
        };
    },
    watch: {
        msg: function() {
            this.message = this.msg;
        }
    }
};
</script>

<style scoped></style>
