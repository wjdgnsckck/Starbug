


import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import './Index.css';
import axios from 'axios';
import starBugLogo from './img/스타버그.png';
import img01 from './img/01.png';
import img02 from './img/02.png';
import img03 from './img/03.png';
import img04 from './img/04.png';
import img05 from './img/05.png';
import img06 from './img/06.png';



export default function Header( props ){

    // 메인에서 최초 시작일땐 매개변수 받아오기
    const[ headerCategoryParams, setheaderCategoryParams ] = useSearchParams();
    const cno = headerCategoryParams.get('headerCategory');
    useEffect(() => {
            if (cno) {
                setHeaderCategory(parseInt(cno, 10));
            }
        }, []);

    const [ headerCategory, setHeaderCategory ] = useState(1);


    // 메인과 포스에서는 헤더가 노출되지 않게 하기
    if (window.location.pathname === '/pos' ||
        window.location.pathname === '/main'
    ) { return null; }

    // <img src= { img05 }/>


        return(<>
        <div className="sideBar">
                <ul className="reactUL">

                    <li className={headerCategory === 1? 'activeHeaderCategory' : ''}>
                    <Link to='/sale' className={headerCategory === 1? 'activeHeaderCategoryA' : ''}
                            onClick={ (e) => { setHeaderCategory(1) } }
                         > { headerCategory === 1? <img src= { img01 } style={{ width:"30px"}}/> : ''} 매입매출관리 </Link> </li>

                    <li className={headerCategory === 2? 'activeHeaderCategory' : ''}>
                    <Link to='/staff' className={headerCategory === 2? 'activeHeaderCategoryA' : ''}
                            onClick={ (e) => { setHeaderCategory(2) } }
                         > { headerCategory === 2? <img src= { img02 } style={{ width:"18px"}}/> : ''} 인사관리 </Link> </li>

                    <li className={headerCategory === 3? 'activeHeaderCategory' : ''}>
                    <Link to='/member' className={headerCategory === 3? 'activeHeaderCategoryA' : ''}
                            onClick={ (e) => { setHeaderCategory(3) } }
                         > { headerCategory === 3? <img src= { img03 } style={{ width:"22px"}}/> : ''} 회원관리 </Link> </li>

                    <li className={headerCategory === 4? 'activeHeaderCategory' : ''}>
                    <Link to='/product' className={headerCategory === 4? 'activeHeaderCategoryA' : ''}
                            onClick={ (e) => { setHeaderCategory(4) } }
                         > { headerCategory === 4? <img src= { img04 } style={{ width:"30px"}}/> : ''} 상품관리 </Link> </li>

                    <li className={headerCategory === 5? 'activeHeaderCategory' : ''}>
                    <Link to='/inventory/category' className={headerCategory === 5? 'activeHeaderCategoryA' : ''}
                            onClick={ (e) => { setHeaderCategory(5) } }
                         > { headerCategory === 5? <img src= { img05 } style={{ width:"31px"}}/> : ''} 재고관리 </Link> </li>

                    <li className={headerCategory === 6? 'activeHeaderCategory' : ''} >
                    <Link to='/parcelpage' className={headerCategory === 6? 'activeHeaderCategoryA' : ''}
                            onClick={ (e) => { setHeaderCategory(6) } }
                         > { headerCategory === 6? <img src= { img06 } style={{ width:"32px"}}/> : ''} 발주관리 </Link> </li>


                    <li style={{ paddingTop: "310px", paddingLeft: "22px" }}> <a href='/main'>
                    <img src= { starBugLogo } style={{borderRadius: "77px", width:"150px"}}/> </a> </li>


                </ul>


        </div>
        </>)
}