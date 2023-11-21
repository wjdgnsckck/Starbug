import { useState, useEffect } from 'react';
import axios from 'axios';
import style from './Coupon.css';
import CouponList from './Couponlist.js';
import Pagination from '@mui/material/Pagination'; //페이징처리


export default function Coupon(props){
    let [ reRendering , setRerendering ] = useState(0); // 상태변수 선언

    // 쿠폰등록 버튼 클릭시 실행되는 함수
    const couponSignup = ()=>{
        let ccdateInput = document.querySelector('.ccdate').value;
        let cccontentInput = document.querySelector('.cccontent').value;
        let ccpercentInput = document.querySelector('.ccpercent').value;
        if (ccdateInput=='' || cccontentInput=='' || ccpercentInput==''){
            alert('빈칸 없이 입력해주세요');
            return;
        }
        let info = {
            ccdate : ccdateInput ,
            cccontent : cccontentInput ,
            ccpercent : ccpercentInput
        };

        //console.log(info);

        axios
            .post('/coupon/postcc' , info )
            .then( r => {
                if(r.data) {
                    alert('쿠폰등록 성공');
                    document.querySelector('.ccdate').value ='';
                    document.querySelector('.cccontent').value='';
                    document.querySelector('.ccpercent').value='';
                    setRerendering(prev => prev + 1);  // 쿠폰 등록 성공시 reRendering값을 증가시켜 재랜더링
                } else {
                    alert('쿠폰등록 실패');
                }
            });

    }

    return(<>
        <div className="couponContainer">
            <div className="couponinsertArea">
                <input type="text" className="cccontent" placeholder="쿠폰내용" />
                <input type="text" className="ccpercent" placeholder="할인율(%) 숫자만 입력" />
                <input type="text" className="ccdate" placeholder="기한(일) 숫자만 입력" />

                <button type="button" className="btnCouponSignup" onClick={couponSignup}>쿠폰 등록</button>
            </div>
            <CouponList reRendering={reRendering} /> {/*CouponList 컴포넌트에 매개변수전달 */}

        </div>

    </>)
}

