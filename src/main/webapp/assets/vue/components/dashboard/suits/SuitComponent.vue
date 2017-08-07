<template>
    <div id="box-table-suits">
        <div class="col-md-6 col-sm-6 col-xs-12">
            <div class="x_panel">
                <div class="x_title">
                    <h2>{{ titlePage }}</h2>
                    <ul class="nav navbar-right panel_toolbox">
                        <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                        </li>
                    </ul>
                    <div class="clearfix"></div>
                </div>
                <div class="x_content">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <td v-if="list.length == 0">
                            <h5 style="padding-left: 15px;">Haven't any suits.</h5>
                        </td>
                        <view-suit v-else v-for="(suit, index) in list" :suit="suit"
                                   @suitRemoved="removeFromList(index)"
                                   @suitEdited="getSuitInfo($event)"></view-suit>
                        </tbody>
                    </table>
                    <div class="col-xs-12" style="margin-top: 15px;" v-if="canShowMore">
                        <div class="row" style="text-align: center">
                            <button class="btn btn-default" @click="showMore()" style="display: inline-block">Show More
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-sm-12 col-xs-12">
            <create-suit @suitCreated="getSuitsList()"></create-suit>
        </div>

        <!-- Edit Suit Modal -->
        <div class="modal fade" id="editSuitModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="editSuitModalLabel">Редактировать "{{ editSuit.name }}"</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="suit-name-edit">Suit Name</label>
                            <input type="text" id="suit-name-edit" @keyup.enter="updateSuit()" v-model="editSuit.name"
                                   :value="editSuit.name" placeholder="Введите название услуги" class="form-control">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="button" @click="updateSuit()" class="btn btn-primary">Save changes</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style>
    .panel_toolbox {
        min-width: inherit;
    }
</style>

<script>
    var loading_box = '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>';

    import CreateSuit from './CreateSuitComponent.vue'
    import ViewSuit from './ViewSuitComponent.vue'

    export default {
        props: ['titlePage'],

        data() {
            return {
                list: [],
                canShowMore: false,
                count: 0,
                currentPage: 1,
                step: 10,
                disable: false,
                editSuit: {
                    id: 0,
                    name: ''
                }
            }
        },

        mounted: function () {
            this.setDisable();
            this.getSuitsList();
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
             * Process data for request.
             */
            processRequest(suits, replace) {
                console.log("processRequest suits:");
                console.log(suits);

                if (suits === undefined) {
                    this.unsetDisable();
                    return;
                }

                if (replace === true) {
                    this.list = suits;
                    this.currentPage = 1;
                } else {
                    for (var i = 0; i < suits.length; i++) {
                        this.list.push(suits[i]);
                    }
                }

                this.setCount(this.list.length);
                this.handleShowMoreBtn(suits.length);

                if (this.isDisabled()) {
                    this.unsetDisable();
                }
            },

            /**
             * Set editing suit for modal.
             *
             * @param suit
             */
            setEditingSuit(suit) {
                this.editSuit.id = suit.id;
                this.editSuit.name = suit.name;
            },

            /**
             * Unset editing suit for modal.
             */
            unsetEditingSuit() {
                this.editSuit.id = 0;
                this.editSuit.name = '';
            },

            /**
             * Get suit info form modal.
             *
             * @param suit
             */
            getSuitInfo(suit) {
                this.setEditingSuit(suit);
            },

            /**
             * Update suit model.
             */
            updateSuit() {
                var vm = this;

                var data = {
                    id: this.editSuit.id,
                    name: this.editSuit.name
                };

                if (this.isDisabled())
                    return;

                this.setDisable();

                $.post('/ajax/dashboard/suits/update', data)
                        .done(function (data) {
                            if (data.success === true) {
                                var messages = data.messages;

                                $.each(messages, function (key, value) {
                                    toastr.success(value, 'Success')
                                });

                                $('#editSuitModal').modal('hide');

                                vm.getSuitsList();
                            } else {
                                toastr.error('Что-то пошло не так...', 'Error')
                            }
                        })
                        .fail(function (data, statusText, xhr) {
                            // error callback
                            var errors = data;
                            $.each(errors, function (key, value) {
                                if (data.status === 422) {
                                    toastr.error(value[0], 'Error')
                                } else {
                                    toastr.error(value, 'Error')
                                }
                            });

                            vm.unsetDisable();
                        });
            },

            /**
             * Get suits from storage.
             */
            getSuitsList() {
                this.$http.get('/ajax/dashboard/suits?page=1').then((response) => {
                    this.processRequest(response.data.suits, true);
                });
            },

            /**
             * Show more suits by step.
             */
            showMore () {
                if (this.isDisabled())
                    return;

                this.setDisable();
                this.currentPage++;

                this.$http.get('/ajax/dashboard/suits?page=' + this.currentPage).then((response) => {
                    this.processRequest(response.data.suits, false);
                });
            },

            /**
             * Remove suit by index from list.
             */
            removeFromList(index) {
                this.list.splice(index, 1);
            }
        },

        components: {
            'create-suit': CreateSuit,
            'view-suit': ViewSuit
        }
    }
</script>