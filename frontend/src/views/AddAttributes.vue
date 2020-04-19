<template>
        <div>
            <h1>Add your own key to crossword using below formule </h1>
            <attribute></attribute>
        </div>
</template>

<script>
import AddAttribute from '../components/AddAttribute';

export default {
    name: 'Attribute',
    components: {
        attribute: AddAttribute
    },
    data() {
        return {
            childData: JSON,
        };
    },
    methods: {
        updateMessage(variable) {
            this.childData = variable;
        },
    }
};
</script>

<style>
h1{
    text-align: center;
}
</style>