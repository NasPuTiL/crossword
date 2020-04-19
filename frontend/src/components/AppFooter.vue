<template>
    <v-card class="footer">
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

<style>
.footer {
  background: #ffab62;
  width: 100%;
  height: 100px;
  position: absolute;
  bottom: 0;
  left: 0;
}
</style>
