//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ mui table ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

// 테이블 페이징 처리
import Pagination from '@mui/material/Pagination';
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ mui table ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

import Address from './Address';
import style from './StaffRegist.css';

import { BrowserRouter , Routes , Route , Link , useSearchParams , useParams } from 'react-router-dom';
import { useEffect , useState } from 'react';
import axios from 'axios';

// useState 에 초기값을 설정해놓거나, 삼항 연산자 사용해서 null 일때 어떤게 표시될지 설정 , 이왕이면 초기값 넣어 놔라


export default function StaffUpdate(props){

    const sno = props.item;
    console.log('sno 뭐죠' + sno)
    // 최대 글자수 설정 함수
    function maxlength(object){
        if (object.value.length > object.maxLength){
          object.value = object.value.slice(0, object.maxLength);
      }
    }
    // sInfoGet 메소드에서 가져온 개별정보 담는다
    const [sinfo , setSinfo] = useState ( { } )

    // 컴포넌트 호출시 실행 / 개별정보 호출
    const sInfoGet = (e) => {
        axios
            .get( "/staff/staffInfoGet" , { params : {sno} } )
            .then(r=>{console.log(r);
                setSinfo(r.data);
            })
    }



    useEffect( ()=> {sInfoGet()} , [] )

    //---------------------------------주소-------------------------
    const [enroll_company, setEnroll_company] = useState({
        address:'',
    });

    const [popup, setPopup] = useState(false);

    const handleInput = (e) => {
        setEnroll_company({
            ...enroll_company,
            [e.target.name]:e.target.value,
        })
    }

    const handleComplete = (data) => {
        setPopup(!popup);
    }

    //---------------------------------------------------------------//


    // 개별 인사정보 수정
    const staffUpdate = (e) => {
        const staffForm = document.querySelectorAll('.staffForm')[0];
        const staffFormData = new FormData(staffForm);
        staffFormData.set('sno' , sno);


        axios
            .put('/staff/staffPut' , staffFormData)
            .then( r=>{
                if(r.data){
                alert('수정 완료');
                }
                else{alert('수정에 실패 했습니다')}
            })
    }


    // 인사정보 삭제

    const staffDelete = (e) => {
        let infoDelete = prompt('삭제 하려면 "인사정보삭제" 를 입력 하세요')
        if(infoDelete=='인사정보삭제'){
            axios
                .delete("/staff/staffDelete" , {params : {sno} })
                .then(r=>{
                    if(r){ alert('정보 삭제 완료')
                        window.location.href='/staff';
                    }
                    else{alert('삭제 실패')}
                })
        }
    }


    // 이미지 수정
    const imgUpdate = (e) =>{
        const imgForm = document.querySelectorAll('.imgForm')[0];
        console.log(imgForm)
        const imgFormData = new FormData(imgForm);
        imgFormData.set('sno' , sno)

        console.log(imgFormData)

        axios
            .put("staff/staffImgUpdate" , imgFormData)
            .then(r=>{console.log('수정 r 확인'); console.log(r);
                if(r.data){alert('수정 성공'); window.location.href="/staff?headerCategory=2";}
                else{alert('수정 실패')}
            })

    }


return(<>

            <form className="staffForm">

             <div className="sTop">

                {/*이미지 구역*/}
                <div className="simgPalce">
                    <img src={sinfo.sfile != null ? "http://localhost:80/static/media/"+sinfo.sfile : "http://localhost:80/static/media/default.png"} />
                </div>


                {/* 첫번재 구역*/}

                <div className="splace1">
                    <div>이름</div>
                    <div>주민번호</div>
                    <div>연락처</div>
                    <div>결혼</div>
                    <div>생일</div>
                </div>

                {/* onChange={ (e)=>{ setInfo( {...info , sname : e.target.value }) }} */}

                {/*두번째 구역 */}
                <div className="splace2">
                <input className="sname" type="text" value={sinfo.sname} disabled/>
                <div className="sidnumCss">
                <input className="sidnum1" type="text" value={sinfo.sidnum1} disabled/>-
                <input className="sidnum2" type="text" placeholder="*******" disabled />
                </div>
                <input className="sphone" type="text" name="sphone" value={sinfo.sphone} maxlength={13} onChange={ (e)=>{ setSinfo( {...sinfo , sphone : e.target.value }) }}/>
                    <div className="sRadio">
                        <input type="radio" className="smarry" name="smarry" value="기혼" checked={sinfo.smarry == "기혼" ? true : ''} onChange={ (e)=>{ setSinfo( {...sinfo , smarry : e.target.value }) }} />기혼
                        <input type="radio" className="smarry" name="smarry" value="미혼" checked={sinfo.smarry == "미혼" ? true : ''} onChange={ (e)=>{ setSinfo( {...sinfo , smarry : e.target.value }) }} />미혼
                    </div>
                    <div className="sRadio">
                        <input type="radio" className="ssorl" name="ssorl" value="양력" checked={sinfo.ssorl == "양력" ? true : ''} onChange={ (e)=>{ setSinfo( {...sinfo , ssorl : e.target.value }) }} />양력
                        <input type="radio" className="ssorl" name="ssorl" value="음력" checked={sinfo.ssorl == "음력" ? true : ''} onChange={ (e)=>{ setSinfo( {...sinfo , ssorl : e.target.value }) }} />음력
                    </div>
                </div>


                {/* 세번째 구역 */}
                <div className="splace3">
                    <div>직급</div>
                    <div>이메일</div>
                    <div>비상연락처</div>
                    <div>성별</div>
                    <div>입사일</div>
                </div>


                {/* 네번째 구역*/}

                <div className="splace4">
                   <select className="sposition" name="sposition" value={sinfo.sposition} onChange={ (e)=>{ setSinfo( {...sinfo , sposition : e.target.value }) }}>
                        <option value="사원" >사원</option>
                        <option value="대리" >대리</option>
                        <option value="과장" >과장</option>
                        <option value="점장" >점장</option>
                   </select>
                       <input className="semail" type="email" name="semail" value={sinfo.semail} onChange={ (e)=>{ setSinfo( {...sinfo , semail : e.target.value }) }}/>
                       <input className="sephone" type="text" name="sephone" value={sinfo.sephone} onChange={ (e)=>{ setSinfo( {...sinfo , sephone : e.target.value }) }} maxlength={13} />
                   <div className="sRadio">
                       <input type="radio" className="ssex" name="ssex" value="남성"
                         onChange={ (e)=>{ setSinfo( {...sinfo , ssex : e.target.value }) }} checked={sinfo.ssex == "남성" ? true : ''} />남성
                       <input type="radio" className="ssex" name="ssex" value="여성"
                         onChange={ (e)=>{ setSinfo( {...sinfo , ssex : e.target.value }) }} checked={sinfo.ssex == "여성" ? true : ''}  />여성
                   </div>
                   <input className="sjoined" type="date" name="sjoined" value={sinfo.sjoined} disabled/>
                </div>
             </div>

                {/* 주소 출력 구역 */}

        <div className="sBottom">
                <div className="splace1">
                    <div>주소</div>
                    <div>상세주소</div>

                </div>

                <div className="saplace">
                    <input className="user_enroll_text" type="text" required={true} name="saddress"
                    onChange={handleInput} value={enroll_company.address} placeholder={sinfo.saddress} onChange={ (e)=>{ setSinfo( {...sinfo , saddress : e.target.value }) }}/>

                    <button onClick={handleComplete}>주소 검색</button>
                    <div>
                        <input className="sdaddress" name="sdaddress" type="text" placeholder={sinfo.sdaddress} onChange={ (e)=>{ setSinfo( {...sinfo , sdaddress : e.target.value }) }}/>

                        <button onClick={staffUpdate} type="button"> 수정 </button>
                        <button onClick={staffDelete} type="button"> 삭제 </button>
                        {popup && <Address company={enroll_company} setcompany={setEnroll_company}></Address>}
                    </div>
                </div>
            </div>

            </form>

                <form className="imgForm">
                        <div className="sEnd">
                            <div className="sEnd1">사진</div>
                            <div>
                                <input className="sdaddress" type="file" name="file"  multiple />
                                <button onClick={imgUpdate} type="button"> 수정 </button>
                            </div>
                        </div>
                </form>
</>)
}


/*



*/