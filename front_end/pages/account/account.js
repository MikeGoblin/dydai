// pages/account/account.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pages:0,
    token:'',
    coin:120000,
    coin_used:900,
    coin_gained:888888,
    maxcount:10,
    allnum:20,
    //这里要记住连接数据时一定要清空这里的草稿交易记录
    orders:[
      /*{
        info:"成功完成订单",
        datetime:"2022-12-01 15:00",
        value:35.00,
      },
      {
        info:"成功完成订单",
        datetime:"2022-12-01 15:00",
        value:-35.00,
      },
      {
        info:"成功完成订单",
        datetime:"2022-12-01 15:00",
        value:35.00,
      },
      {
        info:"成功完成订单",
        datetime:"2022-12-01 15:00",
        value:-35.00,
      },
      {
        info:"成功完成订单",
        datetime:"2022-12-01 15:00",
        value:35.00,
      },
      {
        info:"成功完成订单",
        datetime:"2022-12-01 15:00",
        value:-35.00,
      },*/
    ]
  },
  getUserOrdersInfo(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        /*console.log(res);
        this.setData({
          token: res.data
        })*/
        wx.request({
          url: 'https://api.revocat.tech/user/get-info',
          method:'POST',
          data:{
            token:res.data,
          },
          success:(res)=>{
            //console.log(res);
            //返回要事先判断成功与失败
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              this.setData({
                allnum:res.data.completed_order_count
              })
            }
          }
        })
      }
    })
  },
  getCoinInfo(){
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
                coin:res.data.coin,
                coin_gained:res.data.coin_gained,
                coin_used:res.data.coin_used
              })
            }
          }
        })
      }
    });
  },
  //用于拉取交易记录信息
  getTradeInfo(){
    /*var page = this.data.pages;
    page++;
    this.setData({
      pages:page
    })*/
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/coin/records',
          method:"POST",
          data:{
            token:res.data,
            maxcount:this.data.maxcount,
            page:this.data.pages
          },
          success:(res)=>{
            console.log(res);
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              var opts = this.data.orders;
              for(var i =0;i<res.data.orders.length;i++){
                opts.push(res.data.orders[i]);
              }
              this.setData({
                orders:opts
              })
            }
          }
        })
      }
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    wx.getStorage({
      key: 'token',
      success: (res) => {
        this.setData({
          token: res.data
        })
      }
    })
    this.getTradeInfo()
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
    this.getCoinInfo()
    this.getUserOrdersInfo()
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
    //this.getTradeInfo()
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})