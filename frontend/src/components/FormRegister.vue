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
import VueSession from 'vue-session';

Vue.use(VueSession);

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
            v => (v && v.length >= 8) || 'Hasło musi zawierać conajmniej 8 znaków'
        ],

        /*
        select: null,
        items: [
            'Kobieta',
            'Mężczyzna',
            'Jestem zdałnione coś i nie potrafię się określić!'
        ],
        checkbox: false
        */
    }),

    methods: {
        validate() {
            this.$refs.form.validate();
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
