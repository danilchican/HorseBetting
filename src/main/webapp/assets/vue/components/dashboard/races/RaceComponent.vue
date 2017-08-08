<template>
    <div id="race-horses">
        <div v-if="attached.length == 0" class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
            <h5 style="margin-top:0">Horses are not selected to current race.</h5>
        </div>
        <view-race-horse v-else v-for="(item, index) in attached"
                         :index="index"
                         :race-horse="item"
                         :horses="horses"
                         @removeHorse="removeHorse(index)"></view-race-horse>
        <div class="form-group">
            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                <a style="cursor: pointer; padding: 5px;" @click="appendHorse()" class="btn-primary">Append horse</a>
            </div>
        </div>
    </div>
</template>

<script>
    var loading_box = '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>';

    import ViewRaceHorseBlock from './ViewRaceHorseBlock.vue'

    export default {
        data() {
            return {
                horses: [],
                attached: [],
                disable: false,
                count: 0
            }
        },

        mounted: function () {
            this.setDisable();
            this.getHorsesList();
        },

        methods: {

            /**
             * Set disable for boxes.
             */
            setDisable() {
                $('input').attr('disabled', 'disabled');
                this.disable = true;
                $('#box-table-suits').find('.x_panel').append(loading_box);
            },

            /**
             * Unset disable from box.
             */
            unsetDisable() {
                $('input').attr('disabled', false);
                this.disable = false;
                $('#box-table-suits').find('.overlay').remove();
            },

            /**
             * Check if the request sent.
             */
            isDisabled() {
                return this.disable;
            },

            /**
             * Set count of retrieved data.
             *
             * @param count
             */
            setCount (count){
                this.count = count;
            },

            /**
             * Get the count of suits.
             */
            getCount() {
                return this.count;
            },

            /**
             * Handle showing ShowMore button.
             *
             * @param count
             */
            handleShowMoreBtn (count) {
                this.canShowMore = (count >= this.step);
            },

            /**
             * Get horses from storage.
             */
            getHorsesList() {
                this.$http.get('/ajax/dashboard/horses').then((response) => {
                    this.processRequest(response.data.horses, true);
                });
            },

            /**
             * Process data for request.
             */
            processRequest(horses) {
                console.log("processRequest horses:");
                console.log(horses);

                if (horses === undefined) {
                    this.unsetDisable();
                    return;
                }

                this.horses = horses;
                this.horses.push({id: undefined, name: 'Выберите лошадь'})

                this.setCount(this.horses.length);
                this.handleShowMoreBtn(horses.length);

                if (this.isDisabled()) {
                    this.unsetDisable();
                }
            },

            /**
             * Append horse to attached list.
             */
            appendHorse() {
                this.attached.push({selected: 0, coefficient: 0});
            },

            /**
             * Remove horse from attached list.
             *
             * @param index
             */
            removeHorse(index) {
                this.attached.splice(index, 1);
            }
        },

        components: {
            'view-race-horse': ViewRaceHorseBlock
        }
    }
</script>