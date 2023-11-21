
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from './Header.js';
import starBugLogo from './img/스타버그.png';


export default function Main( props ){


    // 메인에서 눌리는 카테고리 번호 전달하기
     const onHeader = (e) => {
        console.log(e);
        //e.target.value;
        //<Header catagoryFromMain = {cno} />
     }

      // 오늘 날짜를 가져오기
      const today = new Date();

      // 년, 월, 일 추출
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
      const day = String(today.getDate()).padStart(2, '0');

      // 날짜 문자열 생성 (형식: YYYY-MM-DD)
      const formattedDate = `${year}-${month}-${day}`;

    return(<>
            <div className="RoutesBox02">
                <div className="mainBox">
                    <span className="StarBugSystem"> <img src= { starBugLogo } style={{borderRadius: "26px"}}/> StarBugSystem </span>
                       <div className="mainBtnBox">
                           <div className="ownerBox">
                               <div className="ownerBoxContent">
                                   <div className="ownerName"> 커피프린스 1호점 </div>
                                   <div className="ownerDate">{formattedDate}</div>
                               </div>
                               <div className="startPos"> <a href='/pos' > 영업시작 </a> </div>
                           </div>
                           <div className="linkBox">
                                <div className="linkBox_02" style={{ display : "flex" }}>
                                    <a href='/sale?headerCategory=1'  onClick={ onHeader }> <div>매입매출관리 </div></a>
                                    <a href='/staff?headerCategory=2'  onClick={ onHeader } > <div>인사관리</div></a>
                                </div>
                                <div className="linkBox_02" style={{ display : "flex" }}>
                                    <a href='/member?headerCategory=3'  onClick={ onHeader }> <div>회원관리 </div></a>
                                    <a href='/product?headerCategory=4'  onClick={ onHeader }> <div>상품관리 </div></a>
                                </div>
                                <div className="linkBox_02" style={{ display : "flex" }}>
                                   <a href='/inventory/category?headerCategory=5'  onClick={ onHeader }> <div>재고관리 </div></a>
                                    <a href='/parcelpage?headerCategory=6'  onClick={ onHeader }> <div>발주관리 </div></a>
                                </div>
                           </div>
                       </div>
                </div>
            </div>
    </>)
}

