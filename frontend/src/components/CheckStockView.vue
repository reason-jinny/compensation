<template>

    <v-data-table
        :headers="headers"
        :items="checkStock"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'CheckStockView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            checkStock : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/checkStocks'))

            temp.data._embedded.checkStocks.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.checkStock = temp.data._embedded.checkStocks;
        },
        methods: {
        }
    }
</script>

