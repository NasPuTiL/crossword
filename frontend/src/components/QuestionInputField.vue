<template>
    <v-layout justify-center align-center column pa-5>
        <v-col cols="12" md="1" sm="1">
            <div class="hidden-sm-and-down py-12" style="min-height: 20vh;" />
        </v-col>
        <v-spacer class="hidden-sm-and-down"></v-spacer>
        <v-col cols="12" sm="6" md="9">
            <v-text-field
                label="Wpisz pytanie"
                outlined
                color="grey-lighten-6"
                clearable
                hide-details
                v-model="tempMessage"
                @input="submit"
                >{{ tempMessage }}</v-text-field
            >
        </v-col>
    </v-layout>
</template>

<script>
export default {
    name: 'QuestionInputField',
    data() {
        return {
            tempMessage: '',
            value: '',
            info: null,     
        };
    },
    methods: {
        prepareData: function(value){
            return value;
        },
        findResult: function(value){
            this.$http
                    .post(
                        'http://localhost:8080/crossword/findResult',
                        {
                            values: value.split(' '),
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
                                console.log(response.data);
                                this.info = response.data;
                                console.log(this.info);
                            }
                        },
                        function(err) {
                            console.log('err', err);
                        }
                    );
        },
        findDiff: function(str1, str2){ 
                let diff= "";
                str2.split('').forEach(function(val, i){
                    if (val != str1.charAt(i))
                    diff += val ;         
                });
                return diff;
            },
        submit: function() {
            if(this.tempMessage === ''){
                this.value = "";
                this.$emit('inputData', this.value);
            }
            else if(this.tempMessage !== '' && this.tempMessage.length > 2 && this.findDiff(this.value, this.tempMessage).length > 2){
                this.value = this.tempMessage;
                this.findResult(this.value);
                this.$emit('inputData', this.info); 
           }
        },      
    }
};
</script>

<style scoped></style>
