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

import style from './StaffInfo.css';


import { BrowserRouter , Routes , Route , Link , useSearchParams , useParams } from 'react-router-dom';


import axios from 'axios';
import {useState , useEffect} from 'react';

import SalaryUpdate from './SalaryUpdate';


export default function Salary(props){

    // 이름으로 검색한 사원정보 저장
    const [ staff , setStaff] = useState( { } );

    // 급여 정정 저장
    const [ salaryList , setSalaryList ] = useState( { salaryDtos : [] , totalPage : 0 , tcotalCount : 0 }  );

    // 페이징
    const [pageInfo , setPageInfo ] = useState( { page : 1 , sno : 0} );

    useEffect( ()=>{ } , [pageInfo.page])//useEffect // pageInfo 값이 변경될때 axios 실행


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
            .then(r=> { console.log('검색 버튼 입력 후 ');  console.log(r); console.log(r.data) ; console.log(r.data.sname);

                setStaff(r.data);
                pageInfo.sno = r.data.sno;
                setPageInfo({...pageInfo});
                salaryGet();
            })
    }


    //급여 등록
    const salaryPost = (e) =>{

        let salaryForm = document.querySelectorAll('.salaryForm')[0];
        let salaryFormData = new FormData(salaryForm);
        salaryFormData.set('sno' , staff.sno)

        axios
            .post("/staff/salaryPost" , salaryFormData)
            .then(r=>{ console.log(r)
                if(r.data){alert('등록성공'); salaryGet(); window.location.href="/staff?headerCategory=2"; }
                else{alert('등록 실패')}
            } )
    }


    //급여 정보 호출
    const salaryGet = (e) =>{

        axios
            .get("/staff/salaryGet" , {params : pageInfo})
            .then(r=>{ console.log(r.data);
                setSalaryList(r.data);
            })
    }


    // 급여 정보 수정으로 보낼 slno
    const [item , setItem] = useState({slno : 'slno'})



return(<>

    <div className="iwrap">
        <div className="istaffWrap">
            <div className="salaryTop">
                <input type="text" className="sname" placeholder="직원명" />
                <button type="button" onClick={staffSearch} className="serchBtn"> 검색 </button>
                <input type="text" className="sname1" value={staff.sname} disabled/>
            </div>

            <form className="salaryForm" >
                <div className="salaryInput">
                    기본급
                    <input type="text" name="sbasepay" placeholder="기본급" />

                    장려금
                    <input type="text" name="sincentive" placeholder="장려금" />

                    차감
                    <input type="text" name="sdeductible" placeholder="차감" />
                    지급일
                    <input className="tdateCss" type="date" name="sdate" />
                    <button onClick={salaryPost} type="button" className="serchBtn" > 등록 </button>
                </div>
            </form>

            <SalaryUpdate key={item}  item={item} />

            <TableContainer component={Paper}>
              <Table sx={{ minWidth: 650 }} aria-label="simple table">
              {/*테이블 헤더*/}
                <TableHead>
                  <TableRow>

                    <TableCell align="center">기본급</TableCell>
                    <TableCell align="center">장려금</TableCell>
                    <TableCell align="center">차감액</TableCell>
                    <TableCell align="center">총금액</TableCell>
                    <TableCell align="center">지급일</TableCell>
                    <TableCell align="center">수정 및 삭제</TableCell>
                  </TableRow>
                </TableHead>
                 {/*테이블 헤더*/}


                 {/*테이블 내용*/}
                <TableBody>

                  {salaryList.salaryDtos.map((row) => ( // map is not a function
                      <TableRow
                        key={row.sname}
                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
                      <TableCell style={{ width : '10%'}}align="center">{row.sbasepay.toLocaleString()}</TableCell>
                      <TableCell style={{ width : '10%'}}align="center">{row.sincentive.toLocaleString()}</TableCell>
                      <TableCell style={{ width : '15%'}}align="center">{row.sdeductible.toLocaleString()}</TableCell>
                      <TableCell style={{ width : '20%'}}align="center">{ ( ((row.sbasepay)+(row.sincentive))-(row.sdeductible) ).toLocaleString() }</TableCell>
                      <TableCell style={{ width : '15%'}}align="center">{row.sdate}</TableCell>
                      <TableCell style={{ width : '20%'}}align="center">
                      <Button style={ {color :'#9A8C81' , borderColor : '#9A8C81'} } size="small" variant="outlined" onClick={()=>{setItem(row.slno)}} >변경</Button>

                      </TableCell>
                       </TableRow>
                  ))}


                </TableBody>
                 {/*테이블 내용*/}

              </Table>
            </TableContainer>
                <div style={{ display: 'flex' , justifyContent : 'center' , margin : '10px 10px 30px 10px' , alignItems: 'center' }}>
                <Pagination page={pageInfo.page} count={salaryList.totalPage} onChange={onPageSelect} size="large"/>
                </div>

        </div> {/*staffWrap*/}
    </div> {/*swrap*/}
</>)

}


/*
        axios
            .Delete("/staff/salaryDelete" , {params : slno})
            .then(r => {console.log(r)
                if(r.data){alert('삭제 성공')}
                else{alert('삭제 실패')}
            })

              */

