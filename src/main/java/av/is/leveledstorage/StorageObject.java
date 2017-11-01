package av.is.leveledstorage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Leveled Storage 사용을 위한 객체 API 클래스입니다.
 *
 * 이 객체가 스토리지에 저장되며,
 * 인터페이스를 상속하여 저장할 데이터를 설정할 수 있습니다.
 */
public interface StorageObject<I extends InputStream, O extends OutputStream> extends Readable<I>, Writable<O> {

}
