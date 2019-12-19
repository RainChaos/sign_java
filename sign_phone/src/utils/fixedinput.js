export default {
    data() {
        return {
            windowHeight: 0
        }
    },
    mounted() {
        this.windowHeight = window.innerHeight
    },
    methods: {
        temporaryRepair() {
            let that = this
            let windowFocusHeight = window.innerHeight
            if (that.windowHeight == windowFocusHeight) {
                return
            }
            console.log(that.windowHeight + '--' + windowFocusHeight);
            console.log('修复');
            let currentPosition;
            let speed = 1; //页面滚动距离
            currentPosition = document.documentElement.scrollTop || document.body.scrollTop;
            currentPosition -= speed;
            window.scrollTo(0, currentPosition); //页面向上滚动
            currentPosition += speed; //speed变量
            window.scrollTo(0, currentPosition); //页面向下滚动
        },
    }
}
