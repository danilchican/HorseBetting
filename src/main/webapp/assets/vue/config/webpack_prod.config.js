var webpack = require('webpack')

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
		  'vue$': 'vue/dist/vue.min'
		}
	},
	plugins: [
		new webpack.DefinePlugin({
		  'process.env': {
			NODE_ENV: '"production"'
		  }
		}),
		new webpack.optimize.UglifyJsPlugin({
		  compress: {
			warnings: false
		  }
		})
   ]
}