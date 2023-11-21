import { Link } from 'react-router-dom';
import axios from 'axios';
import Header from '../Header.js';
import { useState , useEffect } from 'react'
import styles from './Inventory.css';
//테이블 mui
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
//페이징처리mui
import Pagination from '@mui/material/Pagination';
/*엑셀*/
import excelLogo from '../img/엑셀_로고.png';



export default function Inventory(props){

  let [ rows , setRows ] = useState (
  {resouresCount : [] , totalPage : 0 , totalCount : 0});

  let [ pageInfo , setPageInfo ] = useState ({
  page : 1  , keyword : '' ,view : 5 , serachState : false
   })

  const getResoures = (e) =>{                                                       // spring에 axios 사용을 위한 함수!
    axios.get('/inventory/resoures',{params : pageInfo})                            // get 방식으로 페이지의 정보를 보내준다.
            .then( r =>{console.log(r.data)
            if(r.data!=null){
            setRows(r.data);}                                                       // 검색자료가있으면 저장
            else{alert("검색재료가없습니다.")}})
  }
    useEffect( ()=>{  getResoures();  } , [ pageInfo.page ,pageInfo.view, pageInfo.serachState] ); //page,view,serachState에 변화가 있을때! 재 랜더링 한다.


    const onPageSelect = ( e , value ) =>{                                           //page클릭시 !
    pageInfo.page =value;                                                            //page의 값 value 를 pageInfo.page에 저장
    setPageInfo({...pageInfo});                                                      // 다시 받은 객체를 주소값 초기화 해서 넣어준다! (이유는 주소값 변경시 재랜더링 실행하기 때문)
    }

     const onSearch = ( e ) =>{                                                      //검색 함수
             if (!pageInfo.keyword.trim()) {
                alert('검색어를 입력해주세요.');
              }
              else {setPageInfo( { ...pageInfo  , page : 1 ,serachState:true} );         // 검색시 검색 했다는 serachState를 true로 바꾼다.
                   getResoures();                                                            // 재 랜더링을 위해 getResoures를 호출한다.
                   }
      }
      const noSearch = (e) => {                                                     //검색삭제 클릭히 함수
        setPageInfo( { ...pageInfo , page : 1 , serachState:false,keyword:''});     //검색 삭제시 초기 페이지를 가기위해 값을 넣어준다.
        getResoures()                                                               //재 랜더링을 위해 getResoures를 호출한다.
      }




    return(<>
        {  /*  예지 : bodyContainer DIV로 전체 틀의 크기 잡아놨습니다.  */ }
        <div className="inventoryWrap">
            <div className="inventoryTop">
                <div className="pageComment">재고(재료)현황</div>
                    <div className="inventoryTopRight">
                    <a href={"/excel/ResouresExcel"}>  <img src={excelLogo} className="excelImg excelImgs" />
                    </a>
                    <div className="selectComment">Resoures View Count</div>

                     <select
                                 value = { pageInfo.view }
                                 onChange={ (e)=>{  setPageInfo( { ...pageInfo , view : e.target.value} );  } }
                                 >
                                 <option value="5"> 5 </option>
                                 <option value="10"> 10 </option>
                     </select>
                     </div>
                </div>
                <div className="inventoryContent">
                <TableContainer component={Paper}>
                                  <Table className="table" sx={{ minWidth: 1100 }} aria-label="simple table" >
                                  {/* 테이블 제목 구역 */}
                                    <TableHead className="inventoryHeader">
                                      <TableRow>
                                        <TableCell align="center"> 재료 번호</TableCell>
                                        <TableCell align="center"> 카테고리</TableCell>
                                        <TableCell align="center"> 재료 이름</TableCell>
                                        <TableCell align="center"> 재료 가격</TableCell>
                                        <TableCell align="center"> 재료 개수</TableCell>
                                      </TableRow>
                                    </TableHead>
                                    {/* 테이블 내용 구역 */}
                                    <TableBody className="inventoryBody">
                                       {rows.resouresCount.map ((row) => (
                                        <TableRow key={row.name} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                          <TableCell  align="center"> {row.resno} </TableCell>
                                          <TableCell  align="center"> {row.rescname} </TableCell>
                                          <TableCell  align="center"> {row.resname} </TableCell>
                                          <TableCell  align="center"> {row.resprice} </TableCell>
                                          <TableCell  align="center"> {
                                          row.rescount >0 ? row.rescount : 0
                                          } </TableCell>

                                        </TableRow>
                                      ))}
                                    </TableBody>
                                  </Table>
                                </TableContainer>
                 </div>
                 {/*페이징 처리 구역*/}
                 <div className="pagewrap">
                    <div className="page">
                    <Pagination page ={pageInfo.page} count={ rows.totalPage } onChange={onPageSelect} color="primary" />
                    </div>
                {/* 검색 기능*/}
                    <div className="search">
                                    <input type="text" placeholder="재료이름을 입력해주세요" maxlength = "8"
                                     onChange = {
                                           (e)=>{ setPageInfo( { ...pageInfo , keyword : e.target.value } )  }
                                                    }/>
                                <button type="button" onClick={ onSearch }>검색 </button>
                                {
                                   pageInfo.serachState ?(<>
                                   <div className="searchDelete">
                                   <button type="button" onClick ={noSearch}>검색제거</button>
                                   </div></>) : (<></>)
                                }
                    </div>

                </div>

         </div>

    </>)
}
