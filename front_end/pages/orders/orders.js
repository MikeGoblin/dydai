// pages/orders/orders.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    display:'false',
    isUp:'true',
    maxnum:5,
    token:'',
    orderid:'123456',
    userInfo:[],
    type:0,
    myorderlist:[
      /*{
        title:'不想被饿的挣扎',
        orderid:114514,
        value:12.25,
        phone:'110',
        target_address:'电子科技大学沙河校区菜鸟驿站',
        orderer_address:'第二教学楼104',
        ordertime:'2003.03.24',
        content:'我快要饿死，然后给我带包辣条谢谢',
        status:'1'
      }*/
    ],
    mytasklist:[
      /*{
        title:'不想被饿的挣扎',
        orderid:114514,
        value:12.25,
        phone:'110',
        status:'3',
        target_address:'电子科技大学沙河校区菜鸟驿站',
        orderer_address:'第二教学楼104',
        ordertime:'2003.03.24',
        content:'我快要饿死,然后给我带包辣条谢谢'
      }*/
    ]
  },
  SearchMyTask(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        /*this.setData({
          token: res.data
        })*/
        wx.request({
          url: 'https://api.revocat.tech/order/received',
          method: 'POST',
          data: {
            token: res.data
          },
          success: (res) => {
            if(res.data.success==0){
              console.log(res.data.msg);
              if(res.data.msg=='没有找到已经接受的订单'){
                this.setData({
                  mytasklist:null
                })
              }
            }else{
              this.setData({
                mytasklist:res.data.mytasks
              })
            }
          }
        })
      }
    })
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
  concel(){
    this.setData({
      display:'false'
    })
  },
  //这个用于删除订单和任务
  DeleteMyOrder(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/order/delete',
          method:'POST',
          data:{
            token:res.data,
            orderid:this.data.orderid
          },
          success:(res)=>{
            //console.log(res);
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              this.onShow();
            }
          }
        })
      }
    });
    this.onShow();
  },
  DeleteMyTask(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        wx.request({
          url: 'https://api.revocat.tech/order/giveup',
          method:'POST',
          data:{
            token:res.data,
            orderid:this.data.orderid
          },
          success:(res)=>{
            console.log(res);
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              this.onShow();
            }
          }
        })
      }
    });
    
  },
  confirm(){
    //删除订单
    wx.getStorage({
      key:'currentorderid',
      success:(res)=>{
        //console.log(res);
        this.setData({
          orderid:res.data
        })
      }
    })
    //获得待删除的id与删除订单类型
    //console.log(this.data.orderid);
    //console.log(this.data.type);
    if(this.data.type==0){
      this.DeleteMyOrder();
    }else{
      this.DeleteMyTask();
    }
    this.setData({
      display:'false'
    })
  },
  myOrder(event){
    //console.log(event.currentTarget.dataset.orderid);
    //设置本地渲染的id
    wx.setStorage({
      key:'currentorderid',
      data:event.currentTarget.dataset.orderid
    })
    wx.navigateTo({
      url: '/pages/myOrder/myOrder',
    })
  },
  myTask(event){
    //console.log(event.currentTarget.dataset.orderid);
    wx.setStorage({
      key:'currentorderid',
      data:event.currentTarget.dataset.orderid
    })
    wx.navigateTo({
      url: '/pages/myTask/myTask',
    })
  },
  delete(event){
    //console.log(event.currentTarget.dataset.orderid);
    wx.setStorage({
      key:'currentorderid',
      data:event.currentTarget.dataset.orderid
    })
    this.setData({
      display:'true',
      type:event.currentTarget.dataset.type,
      orderid:event.currentTarget.dataset.orderid
    })
  },
  //用于拉取我的发布
  SearchMyOrder(){
    wx.getStorage({
      key: 'token',
      success: (res) => {
        /*this.setData({
          token: res.data
        })*/
        wx.request({
          url: 'https://api.revocat.tech/order/user-order',
          method: 'POST',
          data: {
            token: res.data,
            status:0
          },
          success: (res) => {
            console.log(res);
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              this.setData({
                myorderlist:res.data.orders
              })
            }
          }
        })
      }
    })
  },
  //用于拉取我的接取
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
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
    this.SearchMyOrder()
    this.SearchMyTask()
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