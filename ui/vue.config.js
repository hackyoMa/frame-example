module.exports = {
    publicPath: '/frame',
    productionSourceMap: false,
    devServer: {
        port: 8070,
        http2: true,
        https: true,
        open: true,
        host: '127.0.0.1',
        useLocalIp: true
    }
};
