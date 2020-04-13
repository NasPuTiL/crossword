<template>
    <v-container fluid class="input-background" style="max-height: 100vh;">
        <div>
            Panels...
        </div>
    </v-container>
</template>

<script>

export default {
    name: 'Panel',
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
