<template>
    <v-layout justify-center align-center column pa-5>
        <v-col cols="12" sm="6" md="6">
            <v-card class="pa-5">
                <v-form ref="form" v-model="valid" lazy-validation>
                    <v-text-field
                        v-model="name"
                        :rules="nameRules"
                        label="Login"
                        required
                    ></v-text-field>

                    <v-text-field
                        v-model="email"
                        :rules="emailRules"
                        label="E-mail"
                        required
                    ></v-text-field>

                    <v-text-field
                        v-model="passwd"
                        :rules="passwdRules"
                        :type="'password'"
                        label="Hasło"
                        required
                    ></v-text-field>

                    <v-spacer class="mt-5" />

                    <v-btn
                        color="gray lighten-5"
                        class="mr-4"
                        :class="`d-flex justify-end`"
                        @click="submit"
                    >
                        Zarejestruj
                    </v-btn>
                    <!--
                    <v-select
                        v-model="select"
                        :items="items"
                        :rules="[v => !!v || 'Płeć jest wymagana']"
                        label="Płeć"
                        required
                    ></v-select>

                    <v-checkbox
                        v-model="checkbox"
                        :rules="[
                            v => !!v || 'Musisz się zgodzić, aby kontynuować!'
                        ]"
                        label="Zgadzasz się?"
                        required
                    ></v-checkbox>
                    
                    <v-spacer class="mt-5" />
                    <v-btn
                        :disabled="!valid"
                        color="gray lighten-5"
                        class="mr-4"
                        @click="validate"
                    >
                        Sprawdź
                    </v-btn>

                    <v-btn color="gray lighten-5" class="mr-4" @click="reset">
                        Resetuj formularz
                    </v-btn>

                    <v-btn color="gray lighten-5" @click="resetValidation">
                        Resetuj sprawdzenie
                    </v-btn>
                    -->
                </v-form>
            </v-card>
        </v-col>
    </v-layout>
</template>

<script>
//import axios from 'axios';

/*
Vue.use({
    VueSession,
    //VueResource,
});
*/

export default {
    name: 'FormRegister',
    data: () => ({
        valid: true,
        name: '',
        nameRules: [
            v => !!v || 'Login jest wymagany',
            v => (v && v.length >= 5) || 'Login musi być dłuższy niż 5 znaków'
        ],
        email: '',
        emailRules: [
            v => !!v || 'E-mail jest wymagany',
            v => /.+@.+\..+/.test(v) || 'E-mail musi być poprawny'
        ],
        passwd: '',
        passwdRules: [
            v => !!v || 'Hasło jest wymagane',
            v =>
                (v && v.length >= 8) ||
                'Hasło musi zawierać conajmniej 8 znaków'
        ]

        /*
        select: null,
        items: [
            'Kobieta',
            'Mężczyzna',
            'Jestić!'
        ],
        checkbox: false
        */
    }),

    methods: {
        submit() {
            if (this.validate()) {  
                this.register();
            } else {
                console.log('Chuja tam wyszło!');
            }
        },

        register() {
            /*
            const postData = {
                username: this.name,
                password: this.password,
                email: this.email
            };
            
            axios.post(
                    'http://194.28.50.218:8080/crossword/register',
                    postData,
                    {

                        headers: {
                            'Access-Control-Allow-Origin': '*',
                            'Access-Control-Allow-Methods': 'POST, GET, PUT, OPTIONS, DELETE',
                            'Access-Control-Allow-Headers': 'Access-Control-Allow-Methods, Access-Control-Allow-Origin, Origin, Accept, Content-Type',
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        },
                    }
                ).then((response) => {
                    console.log(response);
                }, (error) => {
                    console.log(error);
                });
            */

            this.$http.post('http://194.28.50.218:8080/crossword/register', {
                username: this.name,
                password: this.passwd,
                email: this.email,
            },
            {
                headers: {
                    //'Access-Control-Allow-Origin': 'true',
                    //'Access-Control-Allow-Methods': 'POST, GET, PUT, OPTIONS, DELETE',
                    //'Access-Control-Allow-Headers': 'Access-Control-Allow-Methods, Access-Control-Allow-Origin, Origin, Accept, Content-Type',
                    //'Access-Control-Allow-Headers': 'Accept',
                    //'Accept': 'application/json'
                    'Access-Control-Allow-Headers': 'Content-Type',
                    'Content-Type': 'application/json',

                },
                //emulateJSON: true,
                /*
                proxy: '192.168.100.30',
                port: 8088,
                */
            }).then(function (response) {
                if (response.status === 200) {
                //this.$session.start()
                //this.$session.set('jwt', response.body.token)
                //this.$http.headers.common['Authorization'] = 'Bearer ' + response.body.token
                //this.$router.push('/panel/search')
                console.log('ok', response)
                }
            }, function (err) {
                console.log('err', err)
            })
        },
        // created() {
        //     this.$http
        //         //.get('http://194.28.50.218:8080/crossword/register'),
        //         .get('http://localhost:8080/crossword/register')
        //         .then(res => {
        //             this.post = res.body;
        //         });
        // },
        
          

        

        validate() {
            //console.log(this.$refs.form.validate());
            this.$refs.form.validate();
            return this.$refs.form.validate()
        },
        reset() {
            this.$refs.form.reset();
        },
        resetValidation() {
            this.$refs.form.resetValidation();
        }
    }
};
</script>

<style scoped></style>
