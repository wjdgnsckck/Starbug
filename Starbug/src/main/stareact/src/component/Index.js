
// 리액트 라우터 라이브러리 호출
import {BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import './Index.css';

// 메인 페이지 호출 ( 시스템 메뉴 선택페이지 )

import Main from './Main.js';
import Header from "./Header";

// 인사관리
import StaffRegist from'./staff/StaffRegist';
import StaffList from './staff/StaffList.js';
import StaffUpdate from './staff/StaffUpdate.js';
// 급여 관리
import Salary from './staff/Salary.js';

// 매입매출관리
import Pos from './order/Pos.js';
import SaleCategory from './order/SaleCategory.js';
import SalePrint from './order/SalePrint.js';
import BuyOutPrint from './order/BuyOutPrint.js';
import Chart from './order/SaleAndPurchaseChart.js';
import Income from './order/Income.js';



// 회원관리
import Member from './member/Member.js';
import MemberInsert from './member/Memberinsert.js';
import MemberGet from './member/Memberget.js';
import CouponList from './member/Couponlist.js';
import Coupon from './member/Coupon.js';
import MemberInfo from './member/Memberinfo.js';

// 상품관리
import ProductList from './product/ProductList.js';
import Product from "./product/Product.js";

// 발주관리
import Parcel from './parcel/Parcel.js';
import ParcelView from './parcel/ParcelView.js';
import ParcelOrder from './parcel/ParcelOrder.js';

// 재고관리
import Inventory from './inventory/Inventory.js';
import InventoryCategory from './inventory/InventoryCategory.js';
import InventoryLogInsert from './inventory/InventoryLogInsert.js';
import ResouresUpdate from './inventory/ResouresUpdate.js';
import InventoryLog from './parcel/InventoryLog.js';

// mui 알람창
import { useSnackbar } from 'notistack';

import { useRef , createContext } from 'react'

export const SocketContext = createContext();

export default function Index( props ){

//
    const { enqueueSnackbar } = useSnackbar();

     // 2. 웹소켓
        //----------------------- 소켓s -------------------------
                                                                                                                          // * 웹소켓 객체를 담을 useRef 변수 생성
        let clientSocket = useRef( null );
        // 1. 만약에 웹 소켓 객체가 비어있으면
        if( !clientSocket.current ){
            clientSocket.current = new WebSocket( "ws://localhost:80/alram" );
            // 2. 클라이언트 소켓의 각 기능/메소드 들의 기능 재구현하기
                // 1. 서버소켓과 연동 성공했을때 이후 행동/메세지 정의
                clientSocket.current.onopen = (e) => { console.log(e); };

                // 2. 서버소켓과 세션 오류가 발생했을때 이후 행동/메소드 정의
                clientSocket.current.onerror = (e) => { console.log(e); };
                // 3. 서버소켓과 연동이 끊겼을때 이후 행동 / 메소드 정의
                clientSocket.current.onclose = (e) => { console.log(e); };

                // 4. 서버소켓으로부터 메세지를 받았을때 이후 행동/메소드 정의
                clientSocket.current.onmessage = (e) => {
                console.log(e);

                enqueueSnackbar( e.data , { variant : 'success' })
                };
        }
    //-------------------------------------------------------------

    return(<>
<SocketContext.Provider value={ clientSocket } >
    <BrowserRouter>
        <div className="RoutesBox">
            <Header /> {/* 사이드바 입니다. */}
            <Routes>
                { /*  메인페이지  */}
                <Route path={'/main'}  element = { <Main /> } />
                <Route path={'/pos'} element={ <Pos /> } />

                { /* 매입매출관리 */ }
                <Route path={'/sale'}  element = { <SaleCategory /> } />
                <Route path={'/salePrint'}  element = { <SalePrint /> } />
                <Route path={'/buyoutPrint'}  element = { <BuyOutPrint /> } />
                <Route path={'/spChart'}  element = { <Chart /> } />
                <Route path={'/income'}  element = { <Income /> } />




                {/* 인사관리 */}
                <Route path={'/staff/StaffRegist'}  element = { <StaffRegist /> } />
                <Route path={'/staff'}  element = { <StaffList /> } />
                <Route path={'/staff/StaffUpdate'}  element = { <StaffUpdate /> } />

                {/* 급여관리 */}
                <Route path={'/staff/Salary'}  element = { <Salary /> } />


                {/* 회원관리 */}
                <Route path={'/member'}  element = { <Member /> } />
                <Route path={'/member/insert'} element={ <MemberInsert />} />
                <Route path={'/member/get'} element={ <MemberGet />} />
                <Route path={'/couponcategory'} element={ <Coupon />} />
                <Route path={'/couponlist'} element={ <CouponList />} />
                <Route path={'/memberinfo'} element={ <MemberInfo />} />


                {/* 상품관리 */}
                <Route path={'/product'}  element = { <Product /> } />



                {/* 재고관리 */}
                <Route path={'/inventory/category'}  element = { <InventoryCategory /> } />
                <Route path={'/inventory'}  element = { <Inventory /> } />
                <Route path={'/inventory/insert'}  element = { <InventoryLogInsert /> } />
                <Route path={'/inventory/update'}  element = { <ResouresUpdate /> } />
                <Route path={'/inventory/Log'}  element = { <InventoryLog /> } />



                {/* 발주관리 */}
                <Route path={'/parcelpage'}  element = { <Parcel /> } />
                <Route path={'/parcelview'}  element = { <ParcelView /> } />
                <Route path={'/parcelorder'}  element = { <ParcelOrder /> } />

            </Routes>
        </div>
    </BrowserRouter>
</SocketContext.Provider>
    </>)
}

