<template>
    <v-layout justify-center align-center column pa-5>
        <v-col cols="12" sm="6" md="6">
            <v-card class="pa-5">
                <v-form ref="form" v-model="valid" lazy-validation>
                    <v-text-field
                        v-model="values"
                        label="Give a question"
                        required
                    ></v-text-field>

                    <v-text-field
                        v-model="key"
                        label="Anwser"
                        required
                    ></v-text-field>

                    <v-btn
                        color="gray lighten-5"
                        class="mr-4"
                        :class="`d-flex justify-end`"
                        @click="submit"
                    >
                        Crossword Key
                    </v-btn>
                </v-form>
            </v-card>
        </v-col>
    </v-layout>
</template>

<script>

export default {
    data(){
        return{
            key: '',
            values: '',
            json: null,
        }
    },
    methods: {
        submit() {
            console.log(this.key)
            console.log(this.values)
            if (this.key === '' || this.values === '') {  
                alert('There are no set fields')
            } else {
                this.setKeyAndValues();
            }
        },
        setKeyAndValues(){
                this.$http.post('http://localhost:8080/crossword/setKeyAndValues', {
                key: this.key,
                values: this.values.split(" ")
            },
            {
                headers: {
                    'Access-Control-Allow-Headers': 'Content-Type',
                    'Content-Type': 'application/json',
                },
            }).then(function (response) {
                if (response.status === 200) {
                    console.log(response.data)
                    this.json = JSON.stringify(response.data);
                    alert(this.json);
                }
            }, function (err) {
                console.log('err', err)
            })
        },
    }
}

</script>