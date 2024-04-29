// pages/myOrder/myOrder.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    token:'',
    display:'false',
    orderid:123,
    order:{
      "success": 1,
      "title": "不想被饿的挣扎",
      "content": "快饿死了，来个人帮我拿一下快递。",
      "value": 11.11,
      "target_address": "电子科技大学沙河校区菜鸟驿站",
      "orderer_address": "第二教学楼104",
      "status": 1,
      "phone": "110",
      "orderername": "Aboutime",
      "ordereravartar": "https://www.baidu.com",
      "ordertime":"2003.3.24"
    }
  },
  finishSubmit(){
    //向后台接受任务
    wx.switchTab({
      url: '/pages/orders/orders',
    })
  },
  submit(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/order/check',
          method:'POST',
          data:{
            token:res.data,
            orderid:this.data.orderid
          },
          success:(res)=>{
            console.log(res);
            if(res.success==0){
              console.log(res.msg);
            }
          }
        })
      }
    })
    this.setData({
      display:'true'
    })
  },
  SearchForOrderDetails(){
    wx.getStorage({
      key:'currentorderid',
      success:(res)=>{
        this.setData({
          orderid:res.data
        })
      }
    })
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/order/get-info',
          method: 'POST',
          data: {
            token: res.data,
            orderid:this.data.orderid
          },
          success: (res) => {
            console.log(res);
            if (res.data.success == 0) {
              console.log(res.data.msg);
            } else {
              this.setData({
                order: res.data
              })
            }
          }
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.SearchForOrderDetails()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})