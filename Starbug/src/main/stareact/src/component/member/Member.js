import { useState, useEffect } from 'react';
import { Link , useSearchParams} from 'react-router-dom';
import axios from 'axios';
import style from './Member.css';
import MemberInsert from './Memberinsert.js';
import MemberGet from './Memberget.js';
import Coupon from './Coupon.js';
import Pagination from '@mui/material/Pagination';

export default function Member(props) {

    const [clickState, setClickState] = useState(0);

    useEffect(() => {
        // clickState가 변경될 때마다 실행
        console.log("clickState: " + clickState);
        addSelectCss();
    }, [clickState]);

    function checkClick(num) {
        setClickState(num);
    }

    function addSelectCss(){ // 카테고리클릭시 css 변경
        let btnMember = document.querySelector('.btnMember');
        let btnMemberInsert = document.querySelector('.btnMemberInsert');
        let btnCoupon = document.querySelector('.btnCoupon');

        if (clickState==0) {
            btnMember.classList.add('btnSelect');
            btnMemberInsert.classList.remove('btnSelect');
            btnCoupon.classList.remove('btnSelect');
        } else if(clickState==1){
            btnMember.classList.remove('btnSelect');
            btnMemberInsert.classList.add('btnSelect');
            btnCoupon.classList.remove('btnSelect');
        } else if(clickState==2){
            btnMember.classList.remove('btnSelect');
            btnMemberInsert.classList.remove('btnSelect');
            btnCoupon.classList.add('btnSelect');
        }
    }
    return (
        <>
            <div className="bodyContainer">
                <div className="memberContainer">
                    <div className="categoryArea">
                        <button type="button" className="btnMember" onClick={() => checkClick(0)}>회원 조회</button>
                        <button type="button" className="btnMemberInsert" onClick={() => checkClick(1)}>회원 등록</button>
                        <button type="button" className="btnCoupon" onClick={() => checkClick(2)}>쿠폰</button>
                    </div>
                    <div className="contentArea">
                        {clickState == 0 ? <MemberGet /> : clickState == 1 ? <MemberInsert /> : <Coupon /> }
                    </div>
                </div>
            </div>
      </>
    );
}
