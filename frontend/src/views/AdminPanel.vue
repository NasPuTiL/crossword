<template>
    <div>
    <v-container fluid class="input-background" style="max-height: 100vh;">
        <div>
            <h1>Hello Admin (Tomasz / Fabian)</h1>
            <panel></panel>
        </div>
    </v-container>
    </div>
</template>

<script>
import Panel from '../components/Panel';

export default {
    name: 'Panel',
    components: {
        panel: Panel,
    },
};
</script>
