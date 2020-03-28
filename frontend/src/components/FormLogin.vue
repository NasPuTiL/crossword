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

                    <!-- w późniejszej wersji dodać logowanie username/e-mail
                    <v-text-field
                        v-model="email"
                        :rules="emailRules"
                        label="E-mail"
                        required
                    ></v-text-field>
                    -->

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
                        Zaloguj
                    </v-btn>
                </v-form>
            </v-card>
        </v-col>
    </v-layout>
</template>

<script>
//import axios from 'axios';

export default {
    name: 'FormRegister',
    data: () => ({
        valid: true,
        name: '',
        nameRules: [
            v => !!v || 'Login jest wymagany',
            v => (v && v.length >= 5) || 'Login musi być dłuższy niż 5 znaków'
        ],
        /*
        email: '',
        emailRules: [
            v => !!v || 'E-mail jest wymagany',
            v => /.+@.+\..+/.test(v) || 'E-mail musi być poprawny'
        ],
        */
        passwd: '',
        passwdRules: [
            v => !!v || 'Hasło jest wymagane',
            v =>
                (v && v.length >= 8) ||
                'Hasło musi zawierać conajmniej 8 znaków'
        ]
    }),

    methods: {
        submit() {
            if (this.validate()) {
                this.login();
            } else {
                console.log('Chuja tam wyszło!');
            }
        },

        login() {
            this.$http
                .post(
                    'http://194.28.50.218:8080/crossword/login',
                    {
                        username: this.name,
                        password: this.passwd
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
                            console.log('ok', response),
                            this.$session.start(),
                            this.$session.set('sessionId', response.body.sessionId),
                            this.$session.set('username', response.body.username),
                            this.$session.renew(response.body.sessionId)
                            //console.log(this.$session.get('sessionId'))
                            //this.$session.renew(response.body.sessionId) //id key contains "sess:" before actual sessionID; remember to remove it before sending to API
                        }
                    },
                    function(err) {
                        console.log('err', err);
                    }
                );
        },

        validate() {
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
