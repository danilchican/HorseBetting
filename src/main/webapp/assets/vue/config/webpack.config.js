module.exports = {
     module: { 
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            }
        ]
    },
	resolve: {
		alias: {
		  'vue$': 'vue/dist/vue'
		}
	}
}