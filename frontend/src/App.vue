<template>
    <v-app>
        <div v-if=isAdmin()>
            <appNavAdmin />
        </div>
       <div v-else-if=checkSession()>
            <appNavLogin />
       </div>
       <div v-else>
            <appNav />
       </div>       

        <v-content transition="slide-x-transition">
            <router-view></router-view>
        </v-content>

        <appFooter />
    </v-app>
</template>

<script>
import AppFooter from '@/components/AppFooter';
import AppNavigation from '@/components/AppNavigation';
import AppNavigationAdmin from '@/components/AppNavigationAdmin'
import AppNavigationLogin from '@/components/AppNavigationLogin'

export default {
    name: 'App',
    components: {
        appFooter: AppFooter,
        appNav: AppNavigation,
        appNavLogin: AppNavigationLogin,
        appNavAdmin: AppNavigationAdmin
    },
    methods: {

        checkSession(){
            console.log(this.$session.get("username"));
            if(this.$session.exists() && this.$session.get("username") !== "admin"){
                console.log("USER");
                return true;
            }
            return false;
        },
        isAdmin(){
            if(this.$session.exists() && this.$session.get("username") === "admin"){
               console.log("ADMIN");
               return true;    
            }
            return false;
        },
    },
    //relocate this to new component (KEEP YOUR CODE CLEAN YOU LITTLE SHIT!)
    beforeCreate: function(){
        if(this.$session.exists()){
           this.$http
            .post(
                //'http://194.28.50.218:8080/crossword/',
                'http://localhost:8080/crossword/',
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
                        console.log(response);
                        this.respon = response.data;
                        this.$session.start();
                        this.$session.set("token", response.body.token);
                        console.log("zalogowany, " + this.respon)
                    }else {
                        //delete session
                        console.log('nie zalogowany')
                    }
                }
            );
        }else{
            console.log("SESSION ???? ");
        }
    }
};
</script>

<style></style>
