<template>
    <v-container fluid class="input-background" style="max-height: 100vh;">
        <div>
                Add attribuetess....
        </div>
    </v-container>
</template>

<script>


export default {
    name: 'Attribute',
    components: {

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
