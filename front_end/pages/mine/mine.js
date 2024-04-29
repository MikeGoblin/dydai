// pages/mine/mine.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    isUp:'true',
    uname:'未命名',
    img:'/images/mine.png',
    userInfo:[],
    allnum:0,
  },
  postUserInfo(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        this.setData({
          token: res.data
        })
      }
    })
    wx.request({
      url: 'url',
      method:'POST',
      //这里选择把userinfo全部都给后端
      data:{
        token:this.data.token,
        username:this.data.userInfo.nickName,
        avartar:this.data.userInfo.avatarUrl
      },
      success:(res)=>{
        //返回要事先判断成功与失败
        if(res.success==0){
          console.log(res.msg);
        }else{ 
        }
      }
    })
  },
  allow(){
    wx.getUserProfile({
      desc: '你的授权信息',
      success: (res) => {
        wx.setStorage({
          key:'userInfo',
          data:res.userInfo
        })
        //this.postUserInfo();
        this.setData({
          userInfo:res.userInfo
        })
        wx.setStorage({
          key:'isUp',
          data:'false'
        })
        this.setData({
          isUp:'false'
        })
      }
    })
  },
  goToAccount(){
    wx.navigateTo({
      url: '/pages/account/account',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    wx.getStorage({
      key:'userInfo',
      success:(res)=>{
        this.setData({
          userInfo:res.data,
          uname:res.data.nickName,
          img:res.data.avatarUrl
        })
      }
    })
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
    wx.getStorage({
      key:'isUp',
      success:(res)=>{
        this.setData({
          isUp:res.data
        })
      }
    })
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