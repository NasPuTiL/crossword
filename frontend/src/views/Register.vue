<template>
    <v-container fluid>
        <form-register></form-register>
    </v-container>
</template>

<script>
import FormRegister from '@/components/FormRegister';
//import AppFooter from '@/components/AppFooter';

export default {
    name: 'Register',
    components: {
        FormRegister,
 //       AppFooter
    }
};
</script>

<style scoped></style>
