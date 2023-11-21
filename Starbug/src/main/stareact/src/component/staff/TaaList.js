//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ mui table ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
// 테이블 페이징 처리
import Pagination from '@mui/material/Pagination';
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ mui table ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

import { BrowserRouter , Routes , Route , Link , useSearchParams , useParams } from 'react-router-dom';
import style from './StaffInfo.css';

import axios from 'axios';
import {useState , useEffect} from 'react';

import TaaUpdate from './TaaUpdate';



export default function TaaList(){

    // 이름으로 검색한 직원정보 저장
    const [staff , setStaff] = useState({ });
    console.log(staff.sname);
    // 가져온 급여정보 저장
    const [taaList , setTaaList] = useState({ timeAndAttendanceDtos : [] , totalPage : 0 , tcotalCount : 0 });
    console.log(taaList.timeAndAttendanceDtos);
    //페이징 처리
    const [pageInfo , setPageInfo ] = useState( { page : 1 , sno : 0} );

    useEffect( ()=> {} , [pageInfo.page] )

    // 페이지 번호 클릭
    const onPageSelect = ( e , value )=> {
        pageInfo.page=value; // 클릭한 페이지번호로 pageInfo 를 변경 해준다[기본값은 1]
        setPageInfo({...pageInfo});  // ...~!@~!@  -> 새로고침 [ 상태 변수의 주소값이 바뀌면 재랜더링 ]
    }

    //이름 검색 후 정보 저장 함수
    const staffSearch = (e) =>{
        // 검색 버튼 입력시 page 1 로 변경
    setPageInfo({...pageInfo , page : 1})
        // 검색 위해 sname 호출
    let sname = document.querySelector('.sname').value;
            console.log('가져온 sname : ' + sname);
        axios
            .get("/staff/staffNameGet" , { params : {'sname' :  sname} })
            .then(r=> {
                // 가져온 정보를 useState 저장
                setStaff(r.data);
                // 정보 검색위해 sno 저장
                pageInfo.sno = r.data.sno;
                // 새로고침
                setPageInfo({...pageInfo});
                // 정보 호출 함수 실행
                taaGet();
            })
    }

    // 근태 등록
    const taaPost = (e) => {

        let sno = staff.sno
        console.log('등록 sno '  +sno)
        let info = {
            tstate : document.querySelector('.tstate').value ,
            tdate : document.querySelector('.tdate').value ,
            sno : sno
        }
            axios
                .post("/staff/taaPost" , info)
                .then(r=>{
                    if(r){alert('등록 완료'); window.location.href="/staff?headerCategory=2"; }
                    else{alert('등록 실패')}
                })

    }

    // 근태 정보 호출
    const taaGet = (e) => {
        axios
            .get("/staff/taaGet" , {params : pageInfo})
            .then(r=>{
                setTaaList(r.data)
            })

    }





    // 급여 정보 수정으로 보낼 tno
    const [item , setItem] = useState({slno : 'tno'})




return(<>

    <div className="iwrap">
        <div className="istaffWrap">


            <div className="salaryTop">
                <input type="text" className="sname" placeholder="직원명" />
                <button type="button" onClick={staffSearch} className="serchBtn"> 검색 </button>
                <input type="text" className="sname1" value={staff.sname} disabled/>
            </div>
            <div className="taaTop">
               미준수
               <select className="tstate">
                   <option value="지각">지각</option>
                   <option value="결근">결근</option>
               </select>
               발생일
               <input className="tdate" type="date" className="tdate" />
               <button onClick={taaPost} type="button" className="serchBtn"> 등록 </button>

            <TaaUpdate key = {item} item = {item} />
            </div>




        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 400 }} aria-label="simple table">
          {/*테이블 헤더*/}
            <TableHead>
              <TableRow>
                <TableCell align="center">근태미준수 내역</TableCell>
                <TableCell align="center">발생일</TableCell>
                <TableCell align="center">수정 및 삭제</TableCell>
              </TableRow>
            </TableHead>
             {/*테이블 헤더*/}


             {/*테이블 내용*/}
            <TableBody>
              {taaList.timeAndAttendanceDtos.map((row) => ( // map is not a function
                  <TableRow
                    key={row.sname}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
                  <TableCell style={{ width : '15%'}}align="center">{row.tstate}</TableCell>
                  <TableCell style={{ width : '20%'}}align="center">{row.tdate}</TableCell>
                  <TableCell style={{ width : '20%'}}align="center">
                  <Button style={ {color :'#9A8C81' , borderColor : '#9A8C81'} } size="small" variant="outlined" onClick={()=>{setItem(row.tno)}} >변경</Button>

                  </TableCell>
                   </TableRow>
              ))}

            </TableBody>
             {/*테이블 내용*/}

          </Table>
        </TableContainer>
            <div style={{ display: 'flex' , justifyContent : 'center' , margin : '10px 10px 30px 10px' , alignItems: 'center' }}>
            <Pagination page={pageInfo.page} count={taaList.totalPage} onChange={onPageSelect} size="large"/>
            </div>

        </div> {/*staffWrap*/}
    </div> {/*swrap*/}

</>)

}

/*


              {taaList.timeAndAttendanceDtos.map((row) => ( // map is not a function
                  <TableRow
                    key={row.sname}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
                  <TableCell style={{ width : '30%'}}align="center">{row.tstate}</TableCell>
                  <TableCell style={{ width : '40%'}}align="center">{row.tdate}</TableCell>
                  <TableCell style={{ width : '30%'}}align="center">
                  <Button style={ {color :'#9A8C81' , borderColor : '#9A8C81'} } size="small" variant="outlined" onClick={()=>{setItem(row.tno)}} >변경</Button>

                  </TableCell>
                   </TableRow>
              ))}

*/