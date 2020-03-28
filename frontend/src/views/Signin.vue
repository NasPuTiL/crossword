<template>
    <v-container fluid>
        <v-layout>
            <form-login></form-login>
        </v-layout>
    </v-container>
</template>
<script>
import FormLogin from '@/components/FormLogin';

export default {
    name: 'Signin',
    components: {
        FormLogin
    },
    methods: {
        
    },
    /*
    beforeCreate: function(){
        if(this.$session.exists()){
            this.$http
            .post(
                'http://194.28.50.218:8080/crossword/',
                {
                    //username: this.name,
                    sessionId: this.$session.get('sessionId')
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
                    if (response.status === 200) {
                        console.log(response),
                        //this.$router.push("/")
                    }
                }
                function(err) {
                }
            );
        }
    }
    */
};
</script>
<style scoped></style>
