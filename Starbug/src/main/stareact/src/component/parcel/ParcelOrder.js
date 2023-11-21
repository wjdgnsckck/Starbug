import { Link } from 'react-router-dom';
import axios from 'axios';
import { useState, useEffect } from 'react';
import Header from '../Header.js';
import styles from './Parcel.css';


// 소켓용 임폴트
import { SocketContext } from '../Index.js';
import { useContext } from 'react';

export default function ParcelOrder( props ){

    // 재료 카테고리 저장하는 state
    let [ rescate , setRescate ] = useState( [] );
    useEffect((e) => {
        {/*카테고리는 db 에서 불러온다*/}
        axios.get('/parcel/getrescate')
       .then( (r) => {
           console.log(r.data);
           setRescate(r.data);
       })
    } ,[])

    // 발주하기전 장바구니에 담아놓는 state 배열
    let [basketRes , setBasketRes ] = useState( [] );
    const basketResour = ( resno , resname , resprice ) => {

        // 발주 갯수 -> 온클릭 누른 index 는 resno 로 구분해서 값 가져온다.
        let pacount = document.querySelector(".pcount"+resno).value;

        // 발주갯수 0 이하 또는 null 이면 유효성 검사 처리
        if( pacount <= 0 || pacount == null ){
            alert('올바른 수량을 입력해주세요.');
            return;
        }

        let res ={ resno : resno ,
                 resname : resname ,
                 resprice : resprice ,
                 pacount : pacount.value }

            // 상태변수는 변경이 되어야 재랜더링 해주기 떄문에 새로운 객체(새로운 주소값)로 바꿔서 저장한다.
            setBasketRes( newBasketRes => [
                                    ...newBasketRes ,
                                    { resno , resname , resprice , pacount } ] , [] )
                console.log(basketRes)
    }  //basketResour end

    // 카테고리 선택하면 해당 rescno 와 같은 재료만 저장하는 state
    let [ resour , setResour ] = useState( [] );
    const getResour = (e) => {
        console.log(e);

        // 선택한 카테고리의 재료 목록 불러온다
        axios.get('/parcel/getresour' , {params: { rescno : e }})
                .then( (r) => {
                    console.log(r.data);
                    //let parcelResour = document.querySelector(".parcelResour");
                    //let html = "";
                        setResour(r.data);
                } , []);

    } //getResour end

    const clientSocket = useContext( SocketContext ).current;

    // 발주 성공 여부를 저장하기 위한 변수
    // 성공 = 1 , 실패 0
    let trstate = 0;
    // 발주 등록하는 함수
    // 장바구니에 여러개 있으면 for문 돌려서 하나씩 등록하기 때문에
    // 비동기로 실행하면 응답이 전부 오기도 전에 결과 실행해버림
    // async - await 으로 동기 처리
    const orderPost = async (e) =>{

        for( let i = 0; i< basketRes.length; i++){

            // resno , pacount 보내준다 주문일은 자동저장
            await axios.post("/parcel/postpa" , { resno : basketRes[i].resno ,
                                            pacount: basketRes[i].pacount } )
                .then((r) =>{
                    console.log(r.data);
                    if(r.data==true){
                        // for 문으로 돌기 때문애 state 저장해 놨다가
                        // 마지막 까지 1이면 전부성공
                        trstate = 1;
                    }
                    else{
                        // 중간에 0이면 실패
                        trstate = 0;
                        return;
                    }
                } )

        }// for end
        console.log(trstate)
        if(trstate == 1){
            //clientSocket.send("발주 성공했습니다.")
            alert("발주 성공했습니다.");
            window.location.href="/parcelpage?headerCategory=6"; // 페이지 새로고침 // 전부 재랜더링
        }
        else{
            alert("발주 실패했습니다.")
        }

    }

    // 모달창 띄우기 위한 state
    let [ modalState , setModalState ] = useState( false );

    // 모달창 끄고 닫기 e -> true or false
    const updateModal = (e) =>{
        setModalState( e );
    }

    // 장바구니에 담은거 취소 함수
    const cancleBasket = ( bobject , resno , resname , pacount ) =>{
        console.log( bobject );
        console.log( "info : " + resno , resname , pacount )
        console.log( basketRes );
                                            // filter 비교한것을 제외한 나머지를 새로운 객체를 상태변수에 저장해줄거임
        const updatedBasketRes = basketRes.filter( b =>
          b !== bobject /*||
          b.resno !== resno ||
          b.resname !== resname ||
          b.pacount !== pacount*/
        );

        setBasketRes( updatedBasketRes );

    }

    console.log( rescate );
    return(<>
        { /*  예지 : bodyContainer DIV로 전체 틀의 크기 잡아놨습니다.  */ }


        <div className="bodyContainer">
            <div className="parcelContainer">
                <div className="parcelWrap">
                    <div className="parcelCate"> {/*카테고리 구역*/}
                    <div className="saleH3"> 발주하기 </div>
                        {
                            rescate.map( (rcate) => {
                                return (<>
                                    <div className="cateContent" onClick={ () => getResour(rcate.rescno) } >
                                        {rcate.rescname}
                                    </div>
                                </>)
                            })
                        }

                    </div>{/*카테고리 구역 end*/}
                    <table className="parcelResour">{/*재료 출력구역*/}
                        { resour.length > 0 ?
                            <>
                                <tr className="resourLine">
                                    <td className="resourTd" style={{ width : '10%'}}>no</td>
                                    <td className="resourTd" style={{ width : '35%'}}>상품명</td>
                                    <td className="resourTd" style={{ width : '25%'}}>가격</td>
                                    <td className="resourTd" style={{ width : '15%'}}>수량</td>
                                    <td className="resourTd" style={{ width : '15%'}} >
                                        <button className="orderChackBtn"
                                        type="button"
                                        onClick={ ()=>updateModal(true) }> {/* 모달창(장바구니) 띄우는 버튼*/}
                                            주문확인
                                        </button>
                                        <span className="basketCount">{basketRes.length}</span>{/*장바구니에 들어간 갯수 표시*/}
                                    </td>
                                </tr>
                            </> :
                            <></> }
                       { resour.map( (r) => {
                        return(
                             <tr className="resourLine">
                               <td className="resourTd" style={{ width : '10%'}}>
                                   {r.resno} {/*상품번호*/}
                                </td>
                                <td style={{ width : '35%'}} >
                                   [{r.prname}] - {r.resname} {/*[거래처] - 재료명*/}
                                </td>
                               <td className="resourTd" style={{ width : '25%'}} >
                                   {r.resprice.toLocaleString()} 원 {/*재료가격*/}
                               </td>
                               <td className="resourTd" style={{ width : '15%'}} >
                                   <input type="number"
                                    className={"pcount"+r.resno}
                                    style={{width:'30px' ,height:'30px' }}/>{/* 발주할 갯수 */}
                               </td>
                               <td className="resourTd" style={{ width : '15%'}} >
                                   <button className="basketBtn"
                                   type="button" onClick={ () => basketResour(r.resno ,r.resname , r.resprice) }>
                                       장바구니 {/*장바구니에 담기버튼*/}
                                   </button>
                               </td>
                             </tr>
                        )
                       }) }
                    </table>

                </div>{/*parcelWrap end*/}
            </div>{/*parcelHeader end*/}
        </div>{/*bodyContainer end*/}
        { modalState == true ?
        <>
            <div className="parcelModalArea">
                <div className="closeBtnArea">
                    <button type="button"
                     onClick={ () => updateModal(false) }
                     className="closePacelModalBtn">
                        닫기{/*모달창(장바구니에 담기버튼)*/}
                    </button>
                </div>
                <div className="basketContent">
                    <table className="basketArea">
                        <tr className="basketLine">
                            <td className="basketTd" style={{ width :'10%' }} >no</td>
                            <td className="basketTd" style={{ width :'40%' }} >상품명</td>
                            <td className="basketTd" style={{ width :'25%' }} >개수</td>
                            <td className="basketTd" style={{ width :'25%' }} >총가격</td>
                        </tr>
                        { basketRes.map( (b) => {
                            return(
                            <tr className="basketLine">
                               <td className="basketTd" style={{ width :'10%' }} >
                                   {b.resno} {/*재료번호*/}
                                </td>
                                <td className="basketTd" style={{ width :'40%' }} >
                                   {b.resname}{/*재료이름*/}
                                </td>
                               <td className="basketTd" style={{ width :'25%' }} >
                                   수량 : {b.pacount}{/*발주갯수*/}
                               </td>
                               <td className="basketTd" style={{ width :'25%' }} >
                                   {(b.resprice * b.pacount).toLocaleString()} 원{/*주문금액*/}
                                   <button
                                   type="button"
                                   className="cancleBasket"
                                   onClick={ (e) => cancleBasket( b , b.resno , b.resname , b.pacount ) }>
                                    X{/*장바구니 취소버튼*/}
                                   </button>
                               </td>
                            </tr>
                            )}
                        )}

                    </table>
                </div> {/*basketContent end*/}
                <div className="orderBtnArea">
                    <button type="button"
                     onClick={orderPost}
                     className="orderBtn">
                        주문하기{/*발주 버튼*/}
                    </button>
                </div> {/*orderBtnArea end*/}
            </div> {/*모달 구역 end*/}
        </> :
            <></> }
    </>)
}