<template>
    <v-app>
        <app-navigation></app-navigation>

        <v-content transition="slide-x-transition">
            <router-view></router-view>
        </v-content>

        <app-footer></app-footer>
    </v-app>
</template>

<script>
import AppNavigation from '@/components/AppNavigation';
import AppFooter from '@/components/AppFooter';

export default {
    name: 'App',
    components: {
        AppNavigation,
        AppFooter
    },
    //relocate this to new component (KEEP YOUR CODE CLEAN YOU LITTLE SHIT!)
    beforeUpdate: function(){
        if(this.$session.exists()){
            this.$http
            .post(
                'http://194.28.50.218:8080/crossword/',
                {
                    //username: this.name,
                    sessionid: this.$session.get('sessionId')
                },
                {
                    headers: {
                        'Access-Control-Allow-Headers': 'Content-Type',
                        'Content-Type': 'application/json'
                    }
                }
            )
            .then(
                function(response) {
                    if (response.status === 200 && response.body.error != undefined) {
                        console.log(response),
                        console.log(response.body.error),
                        console.log("zalogowany")
                    }else {
                        //delete session
                        console.log('nie zalogowany')
                    }
                }
            );
        }
    }
};
</script>

<style></style>
