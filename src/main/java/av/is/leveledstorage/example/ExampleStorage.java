package av.is.leveledstorage.example;

import av.is.leveledstorage.LeveledStorage;
import av.is.leveledstorage.StorageObject;
import av.is.leveledstorage.tags.IntStorage;

import java.io.*;
import java.net.Socket;

/**
 * Created by Avis Network on 2017-11-01.
 */
public class ExampleStorage {
    
    public static void main(String args[]) throws Exception {
        
        // IntStorage를 저장하는 5 레이어의 스토리지를 생성합니다.
        LeveledStorage<IntStorage> newStorage = LeveledStorage.createEmptyStorage(IntStorage.class, 5);
        
        
        
        
        // Configuration을 이용하여 스토리지 설정이 가능합니다.
        newStorage.getConfiguration().divideFiles = true;
        
        
        
        
        // add(T extends StorageObject) 를 이용하여 데이터를 최상위 레이어에 넣을 수 있습니다.
        newStorage.add(new IntStorage(100));
        
        
        
        
        // processTransition() 을 이용하여 레이어를 한 단계씩 내릴 수 있습니다.
        // asyncTransition 설정에 따라서 비동기/동기 처리가 가능합니다.
        newStorage.processTransition();
        
        
        
        
        // 들어있는 데이터를 시각화 하기 위한 디버그 메소드입니다.
        newStorage.debug();
        
        
        
        
        // 저장될 디렉토리를 설정할 수 있습니다.
        // 기본값은 윈도우 기준 C 드라이브입니다.
        newStorage.setDirectory(new File("C:/"));
        
        
        
        
        // processSave() 를 이용하여 데이터를 파일로서 저장할 수 있습니다.
        // asyncSave 설정에 따라서 비동기/동기 처리가 가능합니다.
        newStorage.processSave();
        
        
        // processSave(layer) 를 이용하여 특정 레이어에 저장된 데이터를 파일로서 저장할 수 있습니다.
        // asyncSave 설정에 따라서 비동기/동기 처리가 가능하며
        // 분할저장 설정이 켜져 있어야 활용이 가능합니다.
        newStorage.processSave(2); // 세 번째 레이어의 데이터를 저장합니다. (0부터 시작)
        
        
        
        
        // 저장된 스토리지를 불러옵니다.
        // 분할되어 저장된 스토리지인 경우, 번호가 적혀있지 않은 스토리지를 설정하시면 됩니다.
        // 확장자는 .ls 입니다.
        newStorage = LeveledStorage.loadStorage(new File("C:/storage.ls"));
        
        
        
        
        // InputStream 에 저장된 스토리지를 불러옵니다.
        // 소켓과 같은 네트워크 상으로 전송되는 스토리지를 받기 위한것이며,
        // 분할 저장은 불가능합니다.
        newStorage = LeveledStorage.loadStorage(new FileInputStream(new File("C:/storage.ls")));
        
        
        
        
        // writeStorage() 를 이용하여 파라미터로 들어가는 DataOutputStream 에 스토리지를 저장합니다.
        // 네트워크상의 공유를 위한 메소드입니다.
        newStorage.writeStorage(new DataOutputStream(new Socket(null).getOutputStream()));
    }
    
    // 테스트 스토리지입니다.
    // 레이어에 저장되는 객체들의 정보를 정의합니다.
    public static class TestStorage implements StorageObject<DataInputStream, DataOutputStream> {
        
        private int id;
        private String name;
        
        public TestStorage(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        // InputStream에서 데이터를 읽어옵니다.
        @Override
        public void read(DataInputStream input) throws IOException {
            id = input.readInt();
            name = input.readUTF();
        }
        
        // OutputStream로 데이터를 저장합니다.
        @Override
        public void write(DataOutputStream output) throws IOException {
            output.writeInt(id);
            output.writeUTF(name);
        }
    }
}
