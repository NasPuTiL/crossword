<template>
    <v-layout justify-center align-center column pa-5>
        <v-spacer class="hidden-sm-and-down"></v-spacer>
        <v-col cols="12" sm="6" md="9">
            <v-card v-for="user in list" :key="user.id" >
                <v-list-item class="test">
                    <v-list-item-title>{{user.id - 100}}. Username: {{ user.username}}, email: {{user.email}}, duration to: {{user.duration}}.</v-list-item-title>
                </v-list-item>
            </v-card>
        </v-col>
    </v-layout>
</template>


<script>

export default {
    name: 'Panel',
    data(){
        return {
            list: [],
        }
    },
   beforeCreate() {
            console.log('test');
            this.$http
                    .get(
                        'http://localhost:8080/crossword/getUsers',
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
                                this.list = response.data;
                                console.log(this.list);
                            }
                        },
                        function(err) {
                            console.log('err', err);
                        }
                    );
    }
};
</script>

<style>
.test{
    background: gray;
    padding: 15px;
    margin-left: center;
    margin-right: center;
    font-weight: bold;
    margin-bottom: 10px;
    height: auto;
}
</style>