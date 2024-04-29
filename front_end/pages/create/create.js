// pages/create/create.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isUp:'true',
    isPublish:"false",
    token:'',
    coin:12,
    address:'',
    phone:'',
  },
  getLastOrder(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        /*this.setData({
          token: res.data
        })*/
        wx.request({
          url: 'https://api.revocat.tech/user/last-info',
          method: 'POST',
          data: {
            token: res.data,
          },
          success:(res)=>{
            console.log(res);
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              this.setData({
                address:res.data.address,
                phone:res.data.phone
              })
            }
          }
        })
      }
    });
  },
  CreateMyOrder(opt) {
    //获取token
    wx.getStorage({
      key: 'token',
      success: (res) => {
        /*this.setData({
          token: res.data
        })*/
        wx.request({
          url: 'https://api.revocat.tech/order/create',
          method: 'POST',
          data: {
            token: res.data,
            title: opt.title,
            content: opt.content,
            value: opt.value,
            target_address: opt.target_address,
            orderer_address: opt.orderer_address,
            phone: opt.phone
          },
          success:(res)=>{
            console.log(res);
            if(res.success==0){
              console.log(res.msg);
            }else{
              wx.setStorage({
                key:'orderId',
                data:res.orderid
              })
            }
          }
        })
      }
    });
  },
  publish(event){
    //提交用
    this.setData({
      isPublish:'true'
    })
    var opt = event.detail.value;
    this.CreateMyOrder(opt);
  },
  returnToHome(){
    wx.switchTab({
      url: '/pages/home/home',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getCoin()
    this.getLastOrder()
  },
  getCoin(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/coin/info',
          method:'POST',
          data:{
            token:res.data
          },
          success:(res)=>{
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              this.setData({
                coin:res.data.coin
              })
            }
          }
        })
      }
    });
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