<template>
    <span>
        <v-navigation-drawer
            app
            v-model="drawer"
            class="grey lighten-5"
            dark
            disable-resize-watcher >
            <v-list>
                <template v-for="(item, index) in items">
                    <v-list-item :key="index">
                        <v-list-item-content class="black--text">
                            {{ item.title }}
                        </v-list-item-content>
                    </v-list-item>
                    <v-divider :key="`divider-${index}`"></v-divider>
                </template>
            </v-list>
        </v-navigation-drawer>
        <v-app-bar app color="grey lighten-5" light>
            <v-app-bar-nav-icon
                class="hidden-md-and-up"
                @click="drawer = !drawer"
            ></v-app-bar-nav-icon>
            <v-spacer class="hidden-md-and-up"></v-spacer>
            <router-link to="/">
                <v-toolbar-title to="/">{{ appTitle }}</v-toolbar-title>
            </router-link>
            <v-spacer class="hidden-sm-and-down"></v-spacer>
            <v-btn
                to="panel"
                depressed
                large
                class="hidden-sm-and-down ml-3 mr-3"
                >{{ 'ADMINISTRATOR' | uppercase }}</v-btn>            
            <v-btn
                to="attribute"
                depressed
                large
                class="hidden-sm-and-down ml-3 mr-3"
                >{{ 'Dodaj pytanie' | uppercase }}</v-btn>
            <v-btn
                to="logout"
                raised
                large
                class="hidden-sm-and-down ml-3 mr-6 lg"
                color="grey lighten-7"
                @click="logout"
                >{{ 'logout' | uppercase }}</v-btn>        
            </v-app-bar>
    </span>
</template>

<script>
export default {
    name: 'AppNavigationAdmin',
    data() {
        return {
            appTitle: 'Crosswords',
            drawer: false,
            items: [
                { title: 'Profile' },
                { title: 'Sign In' },
                { title: 'log out'},
                { title: 'Register' }
            ]
        };
    },
    methods: {
        logout(){
            console.log("AUTHB: " + this.$session.get("token"));
            this.$session.set("token", null);            
            this.$session.clear();
            this.$session.destroy();        
            this.$router.push('/')
        }
    },
    filters: {
        uppercase: function(value) {
            if (!value) {
                return '';
            }

            return value.toString().toUpperCase();
        }
    }
};
</script>

<style scoped>
a {
    color: black !important;
    text-decoration: none;
}
</style>
