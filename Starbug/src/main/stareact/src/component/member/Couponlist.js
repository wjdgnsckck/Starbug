import React, { useState, useEffect } from 'react';
import { useSearchParams } from "react-router-dom";
import axios from 'axios';
import style from './Coupon.css';
import style2 from './Member.css';
import Pagination from '@mui/material/Pagination'; //페이징처리

export default function CouponList(props){

    let [ pageDto , setPageDto ] = useState({
        couponDtos : [] ,
        totalPage : 0 ,
        totalCount : 0
    });

    console.log(pageDto);

    const [ pageInfo , setPageInfo ] = useState({
        page : 1 , view : 8 // view 초기값 10
    });

    // 데이터를 가져오는 함수를 별도로 정의
    const ccGet = () => {
        axios.get('/coupon/getcc', {params : pageInfo})
        .then ((r)=>{
            if (r.data) { setPageDto(r.data);  // 쿠폰 데이터 업데이트
            } else { console.log('호출실패');
            }
        })
    }

    useEffect(() => {
        ccGet();
    }, [props.reRendering, pageInfo]);    // 매개변수로 전달받은 reRendering의 값이 변할때마다 ccGet()함수 실행


    //페이지 번호를 클릭했을때
    const onPageSelect = (e , value ) =>{
        console.log(value);
        pageInfo.page = value; // 클릭한 페이지번호로 변경
        setPageInfo({ ...pageInfo });   // 새로고침 [ 상태변수의 주소값이 바뀌면 재랜더링 ]
    }

    // 쿠폰삭제기능
    const ccDelete = (e , ccno ) =>{
        //console.log("실행됨");
        axios.delete('/coupon/deletecc' , { params: { ccno : ccno}} )
        .then ((r)=>{
            if (r.data){ alert('쿠폰 삭제 성공');
              ccGet(); // 재실행
            } else { alert('쿠폰 삭제 실패');
            }
        });
    }


    return(<>
        <div className="tableArea">
            <table>
                <tr>
                    <th className="mnoWidth">번호</th>
                    <th>쿠폰내용</th>
                    <th className="phoneWidth">할인율</th>
                    <th>사용 기한</th>
                    <th>삭제 버튼</th>
                </tr>
                {
                    pageDto.couponDtos && pageDto.couponDtos.map((row)=>{
                        return(<>
                            <tr key={row.ccno}>
                                <td>{row.ccno}</td>
                                <td>{row.cccontent}</td>
                                <td>{row.ccpercent}%</td>
                                <td>{row.ccdate}일</td>
                                <td><button type="button" className="btnCdelete" onClick={ (e)=> { ccDelete( e, row.ccno )  } }>삭제</button></td>
                            </tr>
                        </>)
                    })
                }
            </table>
            <Pagination count={pageDto.totalPage}  page={pageInfo.page} onChange={ onPageSelect } />
        </div>


    </>)
}

