<template>
    <div class="race-horse-item col-md-12 col-sm-6 col-xs-12">
        <div class="form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12">Horse {{ index + 1 }}</label>
            <button class="btn-danger" @click="removeHorse()">x</button>
            <div class="col-md-6 col-sm-6 col-xs-12">
                <select name="selected-horses" v-model="selected" class="form-control">
                    <option v-for="horse in horses" :value="horse.id">{{ horse.name }}</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3 col-sm-3 col-xs-12">Coefficient</label>
            <div class="col-md-6 col-sm-6 col-xs-12">
                <input id="horse-coefficients" data-parsley-min="1" name="horse-coefficients"
                       required="required" step="0.01" type="number" v-model="coefficient"
                       class="form-control col-md-7 col-xs-12">
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: ['index', 'raceHorse', 'horses'],

        data() {
            return {
                disable: false,
                selected: undefined,
                coefficient: 1
            }
        },
        methods: {

            /**
             * Remove horse.
             */
            removeHorse() {
                this.$emit('removeHorse');
            }
        }
    }
</script>