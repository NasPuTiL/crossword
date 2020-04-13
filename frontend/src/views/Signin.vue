<template>
    <v-container fluid>
        <v-layout>
            <appLogin />
        </v-layout>
    </v-container>
</template>
<script>
import FormLogin from '@/components/FormLogin';

export default {
    name: 'Signin',
    components: {
        appLogin: FormLogin
    },
    methods: {
        
    },
};
</script>
<style scoped></style>
