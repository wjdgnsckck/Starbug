import { Link } from 'react-router-dom';
import axios from 'axios';
import { useState, useEffect } from 'react';
import Header from '../Header.js';
import styles from './ParcelView.css';
import excelLogo from '../img/엑셀_로고.png';


export default function ParcelView(props){
    let totalPrice = 0;
    // 선택한 날짜의 발주목록 출력용 상태관리
    const [ parcelLog , setParcelLog ] = useState( [] );
    // 선택한 날짜 저장용
    const [ padate , setPadate ] = useState( [] );
    // 불러온 날짜( 중복된 날짜는 제거된 ) 출력용 상태관리
    const [ cidate , setCidate ] = useState( [] );

    // 날짜별 발주내역 확인 함수
    const parcelView = (e) => {
        // select box 의 날짜
        let cdate = document.querySelector('.dateSelect').value;
        //console.log(cdate);
        setPadate(cdate);

        // 날짜 보내서 검색한 값 받아온다
        axios.get('/parcel/getpa', {params: { cdate : cdate } })
                .then( (r) => {
                    setParcelLog(r.data);
                    // 총금액은 미리 계산 -> 맨 아래에 출력 위해서
                    for (let i = 0; i < r.data.length; i++) {
                        totalPrice += r.data[i].resprice * r.data[i].pacount;
                    }

                    // table 아래 공간에 넣어주기
                    let priceArea = document.querySelector(".priceArea")
                    priceArea.innerHTML = `<div>총금액 : ${totalPrice.toLocaleString()} 원</div>`;
                    // 날짜 변경될수도 있어서 값 넣어준 다음에는 변수에 0 넣어주기
                    totalPrice = 0;
                })
    }

    let cpDate = "";
    // 중복 제거한 날짜 저장하는 배열
    let cdateList = [];
    // 랜더링 될때 한번 - select에 중복 제거된 날짜 추가 해주는 함수
    useEffect(  () => {
        // 값을 받아오고 실행해줘야되서 async - await 동기처리 - 안하면 날짜 출력 안됨
         axios.get('/parcel/getdate')
        .then( (r) => {
            let dateSelect = document.querySelector(".dateSelect");
            //console.log(r.data);
            // 받아온 날짜 길이까지 for문 돌린다.
            for (let i = 0; i < r.data.length; i++) {
                // ciDate 에 날짜 까지만 잘라서 잠깐 저장 한다
                cpDate = r.data[i].cdate.slice(0 , 10)
                // 중복 검사는 indexof 로 검사 - 배열에 있으면 값의 index 없으면 -1 반환
                let cindex = cdateList.indexOf(cpDate);
                // 없으면
                if( cindex == -1 ){
                    // 배열에 저장
                    cdateList.push( cpDate );
                }
            }

            // 중복 제거된 날짜 배열을 상태관리 변수에 저장
            setCidate( cdateList );
            //console.log("cdateList"+cdateList);
            //console.log("padate"+padate);
        })
    },[]);

    // 도착처리 안된 발주내역 취소 함수
    const cancleParcel = (pano) => {

        if( window.confirm("주문 취소 처리 하시겠습니까?") == true ){
                                                    // 발주 번호 controller 로 보낸다.
            axios.delete("/parcel/deletepa" , { params: { pano : pano } })
            .then((r) =>{

                if(r.data){
                    alert("주문 취소 성공했습니다.");
                    parcelView(); // 취소됐으면 재랜더링
                }
                else{
                    alert("주문 취소 실패 (시스템 오류)");
                }
            })

        }
    }

    // 도착 처리 함수 - 발주번호 매개변수로 받아온다.
    const arriveParcel = (pano) => {
        console.log( "pano : " + pano);
        let panoObject = { 'pano' : pano }

        // pano @RequestBody 라서 객체형식으로 바꿔서 보내준다
        axios.put("/parcel/putpa", panoObject )
        .then( (r) =>{
            if(r.data){
                alert("도착 완료 처리 되었습니다.")
                parcelView(); // 재랜더링
            }
        })
    }

    //  환불 신청함수
    const refundParcel = (pano) => {
        console.log( "pano : " + pano);
        // @RequestBody 라 Object
        let panoObject = { 'pano' : pano }

        axios.put("/parcel/refundpa" , panoObject )
        .then( (r) => {
            if(r.data){
                // db에서 pastate true 로 바꿔주고 5시에 스케쥴러로 처리함
                alert("환불 신청 되었습니다. (환불은 매일 오후 5시에 처리됩니다.)");
                parcelView();
            }
            else{
                alert("환불 신청 실패 (시스템 오류)")
            }
        })

    }

    return(<>

        <div className="bodyContainer"> {/*전체구역*/}

            <div className="parcelLogWrap"> {/*발주내역 페이지 구역*/}

                <div className="dateWrap">{/*날짜 select 구역*/}
                <div className="saleH3"> 발주확인 </div>
                    <select className="dateSelect" onChange={parcelView} >
                        <option>날짜선택</option>
                        { cidate.map( (c) => {
                            return(<>
                                <option value={c}>{c}</option>
                            </>)
                        })}
                    </select>
                    <span>
                        <a href={"/excel/parcellog?cdate="+padate}>
                            <img src={excelLogo} className="excelImg" />
                        </a>
                    </span>
                </div>
                    <table className="parcelLogContent"> {/*발주 내역 출력 구역*/}
                        <tr className="parcelLogLine"> {/*th 구역이지만 bold 체 별로여서 td 태그 사용했음*/}
                            <td className="logTh" style={{ width : '20%' }}>상품명</td>
                            <td className="logTh" style={{ width : '10%' }}>수량</td>
                            <td className="logTh" style={{ width : '15%' }}>개별가격</td>
                            <td className="logTh" style={{ width : '15%' }}>총가격</td>
                            <td className="logTh" style={{ width : '12%' }}>주문일</td>
                            <td className="logTh" style={{ width : '12%' }}>도착일</td>
                            <td className="logTh" style={{ width : '16%' }}>비고</td>
                        </tr>
                        { parcelLog.length != 0 ?
                            parcelLog.map( (p) => {
                                return(
                                    <tr className="parcelLogLine">
                                        <td className="logTd" name="resname" style={{ width : '20%' }}>
                                            {p.resname} {/*상품명*/}
                                        </td>
                                        <td className="logTd" name="pacount" style={{ width : '10%' }}>
                                            {p.pacount} 개{/*발주갯수*/}
                                        </td>
                                        <td className="logTd" name="resprice" style={{ width : '15%' }}>
                                            {p.resprice.toLocaleString()} 원 {/*1개당 가격*/}
                                        </td>
                                        <td className="logTd" style={{ width : '15%' }}>
                                            {(p.resprice*p.pacount).toLocaleString()} 원 {/*합산가격*/}
                                        </td>
                                        <td className="logTd" name="cdate" style={{ width : '12%' }}>
                                            {p.cdate.slice(0 , 10)}{/*주문일 LocalDateTime 형식이라 slice*/}
                                        </td>
                                        <td className="logTd" name="udate" style={{ width : '12%' }}>
                                            { p.cdate == p.udate ? "배송중" : p.udate.slice(0 , 10) }{/*도착일 - 주문일과 같으면 미도착*/}
                                        </td>
                                        <td className="logTd" style={{ width : '16%' }}>
                                            {/*버튼구역 - 주문일 = 도착일 => 미도착 -> 배송완료 , 주문 취소 / 주문일 도착일 다르면 -> 도착 -> 환불신청 */}
                                            { p.cdate == p.udate ?
                                                (<>
                                                <button className="arriveParcelBtn" type="button" onClick={ () => arriveParcel(p.pano) }>
                                                    배송완료
                                                </button>
                                                <button className="cancleParcelBtn" type="button" onClick={ () => cancleParcel(p.pano) } >
                                                    주문취소
                                                </button>
                                                </>)
                                                : p.pastate == true ? (<><span>환불 신청중</span></>) : (<>
                                                <button className="refundParcelBtn" type="button" onClick={ () => refundParcel(p.pano)} >
                                                    환불신청
                                                </button>
                                                </>) }
                                        </td>
                                    </tr>
                                )
                            })
                             : <tr><td> 확인 할 날짜를 선택해 주세요</td> </tr>
                        }
                        <div className="priceArea">{/*총금액 들어가는 공간*/}
                        </div>

                    </table>{/*parcelLogContent end*/}
            </div>
        </div>{/*bodyContainer end*/}
    </>)
}