<template>
    <canvas class="canvas" ref="canvas"/>
</template>

<script>
    export default {
        name: "Particles",
        data() {
            return {
                point: 35,
                canvas: null,
                width: 0,
                height: 0,
                context: null,
                circleArr: []
            };
        },
        mounted() {
            this.canvas = this.$refs.canvas;
            if (window.devicePixelRatio === 1) {
                this.width = this.canvas.offsetWidth;
                this.height = this.canvas.offsetHeight;
                this.canvas.width = this.width;
                this.canvas.height = this.height;
                this.context = this.canvas.getContext('2d');
                this.context.strokeStyle = 'rgba(0,0,0,0.02)';
                this.context.strokeWidth = 1;
                this.context.fillStyle = 'rgba(0,0,0,0.05)';
                for (let i = 0; i < this.point; i++) {
                    this.circleArr.push(this.drawCircle(this.getRandom(this.width, 0), this.getRandom(this.height, 0), this.getRandom(15, 2)));
                }
                this.draw();
                setInterval(() => {
                    this.repeatDraw()
                }, 16);
            } else {
                this.canvas.style.backgroundImage = 'url(\"/static/image/background.png\")';
            }
        },
        methods: {
            getRandom(max, min) {
                return Math.floor(Math.random() * (max - min + 1) + min);
            },
            drawCircle(x, y, r) {
                const circle = [x, y, r, this.getRandom(10, -10) / 40, this.getRandom(10, -10) / 40];
                this.context.beginPath();
                this.context.arc(x, y, r, 0, 2 * Math.PI);
                this.context.closePath();
                this.context.fill();
                return circle;
            },
            drawLine(x, y, _x, _y, o) {
                this.context.beginPath();
                this.context.strokeStyle = 'rgba(0,0,0,' + o + ')';
                this.context.moveTo(x, y);
                this.context.lineTo(_x, _y);
                this.context.closePath();
                this.context.stroke();
            },
            draw() {
                this.context.clearRect(0, 0, this.width, this.height);
                for (let i = 0; i < this.point; i++) {
                    this.drawCircle(this.circleArr[i][0], this.circleArr[i][1], this.circleArr[i][2]);
                }
                for (let i = 0; i < this.point; i++) {
                    for (let j = 0; j < this.point; j++) {
                        if (i + j < this.point) {
                            const cir1 = this.circleArr[i];
                            const cir2 = this.circleArr[i + j];
                            let lineOpacity = 1 / Math.sqrt(Math.pow(Math.abs(cir2[0] - cir1[0]), 2) + Math.pow(Math.abs(cir2[1] - cir1[1]), 2)) * 7 - 0.009;
                            lineOpacity = lineOpacity > 0.03 ? 0.03 : lineOpacity;
                            if (lineOpacity > 0) {
                                this.drawLine(cir1[0], cir1[1], cir2[0], cir2[1], lineOpacity);
                            }
                        }
                    }
                }
            },
            repeatDraw() {
                for (let i = 0; i < this.point; i++) {
                    let cir = this.circleArr[i];
                    cir[0] += cir[3];
                    cir[1] += cir[4];
                    if (cir[0] > this.width) {
                        cir[0] = 0;
                    } else if (cir[0] < 0) {
                        cir[0] = this.width;
                    }
                    if (cir[1] > this.height) {
                        cir[1] = 0;
                    } else if (cir[1] < 0) {
                        cir[1] = this.height;
                    }
                }
                this.draw();
            }
        }
    }
</script>

<style scoped>
    .canvas {
        width: 100%;
        height: 100%;
        background-color: #ffffff;
        position: fixed;
        top: 0;
        left: 0;
        z-index: -99;
        display: inline-block;
    }
</style>