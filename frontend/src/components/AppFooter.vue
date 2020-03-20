<template>
    <v-card>
        <v-footer absolute color="grey lighten-5" class="font-weight-medium">
            <v-col class="text-center" cols="12">
                {{ new Date().getFullYear() }} —
                <strong>{{ appTitle }}&copy;</strong>
            </v-col>
        </v-footer>
    </v-card>
</template>

<script>
export default {
    name: 'AppFooter',
    data() {
        return {
            appTitle: 'Crosswords'
        };
    }
};
</script>

<style scoped>
a {
    color: black !important;
    text-decoration: none;
}
</style>
